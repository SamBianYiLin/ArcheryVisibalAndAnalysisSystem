document.addEventListener("DOMContentLoaded", function () {
    initFadeInCards();
    initNumberAnimation();
    beautifyTimeCells();
    initScoreTrendChart();
});

function initFadeInCards() {
    const elements = document.querySelectorAll(".panel-card, .stat-card");

    elements.forEach((el, index) => {
        el.classList.add("fade-in-up");
        setTimeout(() => {
            el.classList.add("show");
        }, 80 * index);
    });
}

function initNumberAnimation() {
    const numberEls = document.querySelectorAll(".number-animate");

    numberEls.forEach(el => {
        const rawText = el.textContent.trim();

        if (!rawText) return;

        const hasPercent = rawText.includes("%");
        const numericText = rawText.replace("%", "").trim();
        const target = parseFloat(numericText);

        if (isNaN(target)) return;

        animateValue(el, target, hasPercent);
    });
}

function animateValue(element, target, hasPercent) {
    const duration = 900;
    const startTime = performance.now();
    const isFloat = String(target).includes(".");

    function update(now) {
        const progress = Math.min((now - startTime) / duration, 1);
        const current = target * easeOutCubic(progress);

        if (isFloat) {
            element.textContent = current.toFixed(1) + (hasPercent ? "%" : "");
        } else {
            element.textContent = Math.round(current) + (hasPercent ? "%" : "");
        }

        if (progress < 1) {
            requestAnimationFrame(update);
        } else {
            element.textContent = (isFloat ? target.toFixed(1) : target) + (hasPercent ? "%" : "");
        }
    }

    requestAnimationFrame(update);
}

function easeOutCubic(x) {
    return 1 - Math.pow(1 - x, 3);
}

function beautifyTimeCells() {
    const timeCells = document.querySelectorAll(".time-cell");

    timeCells.forEach(cell => {
        const text = cell.textContent.trim();

        if (!text) return;

        cell.textContent = text.replace("T", " ");
    });
}

function initScoreTrendChart() {
    const chartEl = document.getElementById("scoreTrendChart");
    if (!chartEl || typeof Chart === "undefined") {
        return;
    }

    const labels = Array.isArray(window.analysisChartLabels) ? window.analysisChartLabels : [];
    const values = Array.isArray(window.analysisChartValues) ? window.analysisChartValues : [];
    if (!labels.length || !values.length) {
        return;
    }

    new Chart(chartEl, {
        type: "line",
        data: {
            labels,
            datasets: [{
                label: "环数",
                data: values,
                borderColor: "#4f46e5",
                backgroundColor: "rgba(79, 70, 229, 0.12)",
                borderWidth: 2,
                pointRadius: 3,
                pointBackgroundColor: "#4338ca",
                pointHoverRadius: 5,
                fill: true,
                tension: 0.35
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: true
                }
            },
            scales: {
                y: {
                    min: 0,
                    max: 10,
                    ticks: {
                        stepSize: 1
                    }
                }
            }
        }
    });
}
