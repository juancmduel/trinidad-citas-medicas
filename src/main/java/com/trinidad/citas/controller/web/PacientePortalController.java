package com.trinidad.citas.controller.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trinidad.citas.dto.CitaDTO;
import com.trinidad.citas.dto.HistoriaClinicaDTO;
import com.trinidad.citas.dto.PacienteDTO;
import com.trinidad.citas.dto.RegistroPacienteDTO;
import com.trinidad.citas.dto.UsuarioDTO;
import com.trinidad.citas.exception.DuplicateResourceException;
import com.trinidad.citas.model.Atencion;
import com.trinidad.citas.model.EstadoCita;
import com.trinidad.citas.model.OrdenExamen;
import com.trinidad.citas.model.Paciente;
import com.trinidad.citas.model.Receta;
import com.trinidad.citas.model.Rol;
import com.trinidad.citas.model.Usuario;
import com.trinidad.citas.repository.AtencionRepository;
import com.trinidad.citas.repository.CitaRepository;
import com.trinidad.citas.repository.HistoriaClinicaRepository;
import com.trinidad.citas.repository.OrdenExamenRepository;
import com.trinidad.citas.repository.PacienteRepository;
import com.trinidad.citas.repository.RecetaRepository;
import com.trinidad.citas.repository.RolRepository;
import com.trinidad.citas.repository.UsuarioRepository;
import com.trinidad.citas.service.CitaService;
import com.trinidad.citas.service.EspecialidadService;
import com.trinidad.citas.service.HistoriaClinicaService;
import com.trinidad.citas.service.MedicoService;
import com.trinidad.citas.service.PacienteService;
import com.trinidad.citas.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Profile({"web", "default"})
@Controller
@RequiredArgsConstructor
public class PacientePortalController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PacienteService pacienteService;
    private final PacienteRepository pacienteRepository;
    private final HistoriaClinicaService historiaClinicaService;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final CitaService citaService;
    private final CitaRepository citaRepository;
    private final EspecialidadService especialidadService;
    private final MedicoService medicoService;
    private final RolRepository rolRepository;
    private final AtencionRepository atencionRepository;
    private final RecetaRepository recetaRepository;
    private final OrdenExamenRepository ordenExamenRepository;

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("registro", new RegistroPacienteDTO());
        model.addAttribute("titulo", "Registro de Paciente");
        return "portal/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registro") RegistroPacienteDTO dto,
                            BindingResult br, RedirectAttributes ra, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("titulo", "Registro de Paciente");
            return "portal/registro";
        }
        try {
            if (usuarioRepository.existsByUsername(dto.getUsername())) {
                throw new DuplicateResourceException("El nombre de usuario '" + dto.getUsername() + "' ya está en uso. Elija otro.");
            }
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateResourceException("El correo '" + dto.getEmail() + "' ya está registrado. Use otro correo.");
            }
            if (pacienteRepository.existsByDni(dto.getDni())) {
                throw new DuplicateResourceException("El DNI " + dto.getDni() + " ya está registrado en el sistema.");
            }

            Rol rolPaciente = rolRepository.findByNombre("PACIENTE")
                    .orElseThrow(() -> new RuntimeException("Error de configuración: el rol PACIENTE no está definido en el sistema"));

            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setUsername(dto.getUsername());
            usuarioDTO.setPassword(dto.getPassword());
            usuarioDTO.setEmail(dto.getEmail());
            usuarioDTO.setActivo(1);
            usuarioDTO.setRolesIds(Set.of(rolPaciente.getIdRol()));
            UsuarioDTO usuarioCreado = usuarioService.crear(usuarioDTO);

            Usuario usuario = usuarioRepository.findById(usuarioCreado.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Error interno al recuperar el usuario recién creado"));

            PacienteDTO pacienteDTO = new PacienteDTO();
            pacienteDTO.setDni(dto.getDni());
            pacienteDTO.setNombres(dto.getNombres());
            pacienteDTO.setApellidoPaterno(dto.getApellidoPaterno());
            pacienteDTO.setApellidoMaterno(dto.getApellidoMaterno());
            pacienteDTO.setFechaNacimiento(dto.getFechaNacimiento());
            pacienteDTO.setSexo(dto.getSexo());
            pacienteDTO.setTelefono(dto.getTelefono());
            pacienteDTO.setEmail(dto.getEmail());
            pacienteDTO.setDireccion(dto.getDireccion());
            pacienteDTO.setDistrito(dto.getDistrito());
            PacienteDTO pacienteCreado = pacienteService.crear(pacienteDTO);

            pacienteRepository.findByDni(dto.getDni()).ifPresent(p -> {
                p.setUsuario(usuario);
                pacienteRepository.save(p);
            });

            long count = historiaClinicaRepository.count();
            String nroHistoria = String.format("HC-%d-%03d", LocalDate.now().getYear(), count + 1);
            HistoriaClinicaDTO hcDTO = new HistoriaClinicaDTO();
            hcDTO.setIdPaciente(pacienteCreado.getIdPaciente());
            hcDTO.setNroHistoria(nroHistoria);
            historiaClinicaService.crear(hcDTO);

            ra.addFlashAttribute("ok", "Registro exitoso. Ya puedes iniciar sesion.");
            return "redirect:/login";
        } catch (DuplicateResourceException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", "Registro de Paciente");
            return "portal/registro";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            model.addAttribute("titulo", "Registro de Paciente");
            return "portal/registro";
        }
    }

    @GetMapping("/portal/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Paciente paciente = getPacienteFromAuth(auth);
        model.addAttribute("paciente", paciente);
        model.addAttribute("proximasCitas", citaRepository.findByPaciente_IdPacienteOrderByFechaCitaDescHoraInicioDesc(paciente.getIdPaciente())
                .stream().filter(c -> c.getEstado() == EstadoCita.PROGRAMADA || c.getEstado() == EstadoCita.CONFIRMADA || c.getEstado() == EstadoCita.EN_TRIAGE)
                .limit(5).toList());
        model.addAttribute("titulo", "Mi Portal");
        return "portal/dashboard";
    }

    @GetMapping("/portal/mis-citas")
    public String misCitas(Authentication auth, Model model) {
        Paciente paciente = getPacienteFromAuth(auth);
        model.addAttribute("citas", citaService.listarPorPaciente(paciente.getIdPaciente()));
        model.addAttribute("titulo", "Mis Citas");
        return "portal/mis-citas";
    }

    @PostMapping("/portal/citas/{id}/cancelar")
    public String cancelarCita(@PathVariable Long id, RedirectAttributes ra) {
        try {
            citaService.cancelar(id);
            ra.addFlashAttribute("ok", "Cita cancelada correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/portal/mis-citas";
    }

    @GetMapping("/portal/agendar")
    public String agendarForm(Model model) {
        model.addAttribute("cita", new CitaDTO());
        model.addAttribute("especialidades", especialidadService.listarActivas());
        model.addAttribute("medicos", medicoService.listarActivos());
        model.addAttribute("titulo", "Agendar Cita");
        return "portal/agendar";
    }

    @PostMapping("/portal/agendar")
    public String agendar(@Valid @ModelAttribute("cita") CitaDTO dto,
                          BindingResult br, Authentication auth,
                          RedirectAttributes ra, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("especialidades", especialidadService.listarActivas());
            model.addAttribute("medicos", medicoService.listarActivos());
            model.addAttribute("titulo", "Agendar Cita");
            return "portal/agendar";
        }
        try {
            Paciente paciente = getPacienteFromAuth(auth);
            dto.setIdPaciente(paciente.getIdPaciente());
            dto.setCanalReserva("WEB");
            citaService.agendar(dto);
            ra.addFlashAttribute("ok", "Cita agendada exitosamente. Revisa tu correo para la confirmacion.");
            return "redirect:/portal/mis-citas";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("especialidades", especialidadService.listarActivas());
            model.addAttribute("medicos", medicoService.listarActivos());
            model.addAttribute("titulo", "Agendar Cita");
            return "portal/agendar";
        }
    }

    @GetMapping("/portal/historia")
    public String historia(Authentication auth, Model model) {
        Paciente paciente = getPacienteFromAuth(auth);
        var historia = historiaClinicaRepository.findByPaciente_IdPaciente(paciente.getIdPaciente())
                .orElse(null);
        List<Atencion> atenciones = historia != null
                ? atencionRepository.findByHistoriaIdWithRelations(historia.getIdHistoria())
                : List.of();

        Map<Long, List<Receta>> recetasPorAtencion = new java.util.HashMap<>();
        Map<Long, List<OrdenExamen>> ordenesPorAtencion = new java.util.HashMap<>();
        for (var a : atenciones) {
            recetasPorAtencion.put(a.getIdAtencion(), recetaRepository.findByAtencion_IdAtencion(a.getIdAtencion()));
            ordenesPorAtencion.put(a.getIdAtencion(), ordenExamenRepository.findByAtencion_IdAtencion(a.getIdAtencion()));
        }

        model.addAttribute("paciente", paciente);
        model.addAttribute("historia", historia);
        model.addAttribute("atenciones", atenciones);
        model.addAttribute("recetasPorAtencion", recetasPorAtencion);
        model.addAttribute("ordenesPorAtencion", ordenesPorAtencion);
        model.addAttribute("titulo", "Historia Clínica");
        return "portal/historia";
    }

    @GetMapping("/portal/mi-perfil")
    public String miPerfil(Authentication auth, Model model) {
        Paciente paciente = getPacienteFromAuth(auth);
        model.addAttribute("paciente", paciente);
        model.addAttribute("titulo", "Mi Perfil");
        return "portal/mi-perfil";
    }

    @PostMapping("/portal/mi-perfil")
    public String actualizarPerfil(Authentication auth,
                                   @ModelAttribute PacienteDTO dto,
                                   RedirectAttributes ra) {
        try {
            Paciente paciente = getPacienteFromAuth(auth);
            dto.setDni(paciente.getDni());
            dto.setNombres(paciente.getNombres());
            dto.setApellidoPaterno(paciente.getApellidoPaterno());
            dto.setApellidoMaterno(paciente.getApellidoMaterno());
            dto.setFechaNacimiento(paciente.getFechaNacimiento());
            dto.setSexo(paciente.getSexo());
            pacienteService.actualizar(paciente.getIdPaciente(), dto);
            ra.addFlashAttribute("ok", "Perfil actualizado correctamente");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/portal/mi-perfil";
    }

    private Paciente getPacienteFromAuth(Authentication auth) {
        String username = auth.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return pacienteRepository.findByUsuario_IdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado para el usuario: " + username));
    }
}
