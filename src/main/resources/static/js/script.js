/**
 * GYMPRO - Neon Glass Micro-interactions
 * Chart.js configuration and UI interactivity
 */

// This file is kept minimal as Chart.js initialization
// is handled inline within each template that needs it.
// Global utilities and shared functions go here.

document.addEventListener('DOMContentLoaded', function() {
    // Sidebar active link scroll into view
    const activeLink = document.querySelector('nav a.text-primary-fixed');
    if (activeLink) {
        activeLink.scrollIntoView({ block: 'nearest', behavior: 'smooth' });
    }
});
