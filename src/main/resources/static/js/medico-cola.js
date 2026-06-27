/* ============================================================
   TRINIDAD — Medico Cola (Auto-refresh & Notifications + Board)
   ============================================================ */
(function() {
    'use strict';

    var POLL_INTERVAL = 20000;
    var pacientesCache = [];

    document.addEventListener('DOMContentLoaded', function() {
        var isBoardPage = !!document.getElementById('medCardGrid') || !!document.querySelector('.med-empty');
        var hasSidebar = !!document.getElementById('medColaCountSidebar');

        if (!isBoardPage && !hasSidebar) return;

        if (isBoardPage) {
            cargarPacientesIniciales();
        }

        pollCola();
        setInterval(pollCola, POLL_INTERVAL);
    });

    function cargarPacientesIniciales() {
        var cards = document.querySelectorAll('.med-card');
        cards.forEach(function(card) {
            var id = card.getAttribute('data-cita-id');
            if (id) pacientesCache.push(Number(id));
        });
    }

    function pollCola() {
        var url = document.getElementById('medColaCountSidebar')?.getAttribute('data-api-url');
        if (!url) {
            var path = window.location.pathname;
            var ctxEnd = path.indexOf('/', 1);
            var ctx = ctxEnd > 0 ? path.substring(0, ctxEnd) : '';
            url = ctx + '/medico/api/cola';
        }

        fetch(url, { headers: { 'Accept': 'application/json' } })
            .then(function(r) {
                if (!r.ok) throw new Error('HTTP ' + r.status);
                return r.json();
            })
            .then(function(data) {
                var pacientes = Array.isArray(data) ? data : (data.pacientes || []);
                if (Array.isArray(pacientes)) {
                    procesarActualizacion(pacientes, data);
                }
            })
            .catch(function(err) {
                console.debug('[MedCola] Poll skipped:', err.message);
            });
    }

    function procesarActualizacion(nuevos, raw) {
        var nuevosIds = nuevos.map(function(p) { return p.idCita; });
        var isBoardPage = !!document.getElementById('medCardGrid') || !!document.querySelector('.med-empty');

        nuevos.forEach(function(p) {
            if (pacientesCache.indexOf(p.idCita) === -1) {
                if (isBoardPage) mostrarNotificacion(p);
            }
        });

        pacientesCache = nuevosIds;
        actualizarContador(nuevos.length);

        if (isBoardPage && raw) {
            actualizarKPIs(raw);
            actualizarTimestamp();
        }
    }

    function actualizarContador(cantidad) {
        var el = document.getElementById('medColaCount');
        if (el) {
            el.textContent = cantidad;
            el.style.display = cantidad > 0 ? 'inline-flex' : 'none';
        }
        var el2 = document.getElementById('medColaCountSidebar');
        if (el2) {
            el2.textContent = cantidad;
            el2.style.display = cantidad > 0 ? 'inline-flex' : 'none';
        }
    }

    function actualizarKPIs(raw) {
        var total = raw.total !== undefined ? raw.total : 0;
        var alta = raw.countAlta !== undefined ? raw.countAlta : 0;
        var media = raw.countMedia !== undefined ? raw.countMedia : 0;
        var baja = raw.countBaja !== undefined ? raw.countBaja : 0;

        setText('medStatTotal', total);
        setText('medStatAlta', alta);
        setText('medStatMedia', media);
        setText('medStatBaja', baja);
    }

    function actualizarTimestamp() {
        var el = document.getElementById('medColaLastUpdate');
        if (!el) return;
        var now = new Date();
        el.textContent = 'Ult. actualizacion: ' +
            String(now.getHours()).padStart(2, '0') + ':' +
            String(now.getMinutes()).padStart(2, '0') + ':' +
            String(now.getSeconds()).padStart(2, '0');
    }

    function setText(id, val) {
        var el = document.getElementById(id);
        if (el) el.textContent = val;
    }

    function mostrarNotificacion(p) {
        var container = document.getElementById('medToastContainer');
        if (!container) {
            container = document.createElement('div');
            container.id = 'medToastContainer';
            container.className = 'med-toast-container';
            document.body.appendChild(container);
        }

        var urgencia = p.nivelUrgencia || 'BAJA';
        var urgenciaClass = urgencia === 'ALTA' ? 'med-urgencia-alta' :
                            urgencia === 'MEDIA' ? 'med-urgencia-media' : 'med-urgencia-baja';

        var toast = document.createElement('div');
        toast.className = 'med-toast';
        toast.innerHTML =
            '<div class="med-toast-icon ' + urgenciaClass + '"><i class="bi bi-person-fill"></i></div>' +
            '<div class="med-toast-body">' +
                '<strong>' + escHtml(p.nombrePaciente) + '</strong>' +
                '<span>' + (p.horaInicio ? p.horaInicio + ' — ' : '') + 'Consultorio ' + escHtml(p.consultorio || '—') + '</span>' +
                '<span class="med-toast-urgencia ' + urgenciaClass + '">' + urgencia.toLowerCase() + '</span>' +
            '</div>' +
            '<button class="adm-toast-close" onclick="this.parentElement.remove()">&times;</button>';

        container.appendChild(toast);

        setTimeout(function() {
            if (toast.parentElement) {
                toast.style.opacity = '0';
                toast.style.transform = 'translateX(100%)';
                setTimeout(function() { if (toast.parentElement) toast.remove(); }, 400);
            }
        }, 8000);
    }

    function escHtml(str) {
        if (!str) return '';
        var d = document.createElement('div');
        d.textContent = str;
        return d.innerHTML;
    }

})();