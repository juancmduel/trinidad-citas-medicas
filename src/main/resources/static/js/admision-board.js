/* ============================================================
   TRINIDAD — Admision Board (Auto-refresh & UX)
   ============================================================ */

(function() {
    'use strict';

    var POLL_INTERVAL = 30000;
    var pollTimer = null;
    var pacientesCache = [];
    var toastCount = 0;

    /* ── Init ── */
    document.addEventListener('DOMContentLoaded', function() {
        if (!document.getElementById('admCardGrid') && !document.querySelector('.adm-empty')) {
            return; // not on board page
        }
        iniciarAutoRefresh();
    });

    /* ── Start polling ── */
    function iniciarAutoRefresh() {
        pollTimer = setInterval(pollPacientes, POLL_INTERVAL);
    }

    /* ── Poll endpoint ── */
    function pollPacientes() {
        var url = document.getElementById('admBoardData')?.getAttribute('data-api-url');
        if (!url) {
            var base = window.location.pathname.replace(/\/+$/, '');
            url = base + '/api/hoy';
        }

        fetch(url, { headers: { 'Accept': 'application/json' } })
            .then(function(r) {
                if (!r.ok) throw new Error('HTTP ' + r.status);
                return r.json();
            })
            .then(function(data) {
                if (Array.isArray(data)) {
                    procesarActualizacion(data);
                }
            })
            .catch(function(err) {
                console.warn('[AdmBoard] Error polling:', err.message);
            });
    }

    /* ── Process update ── */
    function procesarActualizacion(nuevos) {
        var antiguos = pacientesCache;
        var oldIds = antiguos.map(function(p) { return p.idCita; });
        var nuevosMap = {};
        nuevos.forEach(function(p) { nuevosMap[p.idCita] = p; });

        /* detectar nuevos */
        if (antiguos.length > 0) {
            nuevos.forEach(function(p) {
                if (oldIds.indexOf(p.idCita) === -1) {
                    mostrarToast(p.nombrePaciente, p.consultorio);
                }
            });
        }

        pacientesCache = nuevos;
        actualizarStats(nuevos);
        actualizarTimestamp();
    }

    /* ── Update KPIs ── */
    function actualizarStats(data) {
        var total = data.length;
        var pendientes = data.filter(function(p) { return p.estado === 'PROGRAMADA' || p.estado === 'CONFIRMADA'; }).length;
        var triaje = data.filter(function(p) { return p.estado === 'EN_TRIAGE'; }).length;
        var atencion = data.filter(function(p) { return p.estado === 'EN_ATENCION'; }).length;
        var atendidos = data.filter(function(p) { return p.estado === 'ATENDIDA'; }).length;

        setText('admStatTotal', total);
        setText('admStatPendiente', pendientes);
        setText('admStatTriaje', triaje);
        setText('admStatAtencion', atencion);
        setText('admStatAtendido', atendidos);
    }

    function setText(id, val) {
        var el = document.getElementById(id);
        if (el) el.textContent = val;
    }

    /* ── Timestamp ── */
    function actualizarTimestamp() {
        var el = document.getElementById('admLastUpdate');
        if (!el) return;
        var now = new Date();
        var h = String(now.getHours()).padStart(2, '0');
        var m = String(now.getMinutes()).padStart(2, '0');
        var s = String(now.getSeconds()).padStart(2, '0');
        el.textContent = h + ':' + m + ':' + s;
    }

    /* ── Toast notifications ── */
    function mostrarToast(nombrePaciente, consultorio) {
        toastCount++;
        var container = document.getElementById('admToastContainer');
        if (!container) {
            container = document.createElement('div');
            container.id = 'admToastContainer';
            container.className = 'adm-toast-container';
            document.body.appendChild(container);
        }

        var toast = document.createElement('div');
        toast.className = 'adm-toast';
        toast.innerHTML =
            '<div class="adm-toast-icon"><i class="bi bi-person-fill"></i></div>' +
            '<div class="adm-toast-body">' +
                '<strong>' + escHtml(nombrePaciente) + '</strong>' +
                '<span>' + (consultorio ? 'Consultorio ' + escHtml(consultorio) : 'Nuevo paciente') + '</span>' +
            '</div>' +
            '<button class="adm-toast-close" onclick="this.parentElement.remove()">&times;</button>';

        container.appendChild(toast);

        var idx = toastCount;
        toast.style.animation = 'toastIn 0.35s ease forwards';

        setTimeout(function() {
            if (toast.parentElement) {
                toast.style.opacity = '0';
                toast.style.transform = 'translateX(100%)';
                setTimeout(function() { if (toast.parentElement) toast.remove(); }, 400);
            }
        }, 5000);
    }

    function escHtml(str) {
        if (!str) return '';
        var d = document.createElement('div');
        d.textContent = str;
        return d.innerHTML;
    }

    /* ── Expose search/filter (already defined in template, augment) ── */
    window._admBoardInit = true;

})();
