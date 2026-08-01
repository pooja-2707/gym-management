/*
 * =============================================
 * CUSTOM JAVASCRIPT FOR GYM MANAGEMENT
 * =============================================
 * 
 * WHY: For now, this file is mostly empty.
 * We'll add Chart.js code in Phase 10 (Dashboard) and
 * search/filter functionality in Phase 4 (Member Module).
 * 
 * Having this file ready means our layout.html won't throw
 * a 404 error when it tries to load the script.
 */

// Auto-dismiss alerts after 5 seconds
document.addEventListener('DOMContentLoaded', function() {
    // Find all Bootstrap alerts on the page
    const alerts = document.querySelectorAll('.alert');
    
    alerts.forEach(function(alert) {
        // After 5 seconds, automatically fade out and remove the alert
        setTimeout(function() {
            // Bootstrap's Alert class has a close() method
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) {
                bsAlert.close();
            }
        }, 5000);
    });
});
