/**
 * Trinidad Citas Medicas - Script principal
 * Funcionalidades base compartidas entre todas las vistas.
 */

// ── Notificaciones flash ──────────────────────────────────────────
document.addEventListener('DOMContentLoaded', function () {
    // Auto-ocultar alertas de exito/error después de 5 segundos
    var alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () { alert.remove(); }, 500);
        }, 5000);
    });
});

// ── Confirmación de acciones destructivas ─────────────────────────
function confirmarAccion(mensaje) {
    return confirm(mensaje || '¿Está seguro de realizar esta acción?');
}

// ── Formateo de valores monetarios ────────────────────────────────
function formatearMoneda(valor) {
    return 'S/ ' + parseFloat(valor).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}
