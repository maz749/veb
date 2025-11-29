const WIDTH = 400;
const HEIGHT = 400;
const PADDING = 40;
const CENTER_X = WIDTH / 2;
const CENTER_Y = HEIGHT / 2;

window.onload = function() {
    drawGraph();
};

function drawGraph() {
    const canvas = document.getElementById('graphCanvas');
    if (!canvas || !canvas.getContext) {
        return;
    }

    const ctx = canvas.getContext('2d');
    const rElement = document.querySelector('[id$="rSlider_input"]');
    let r = rElement ? parseFloat(rElement.value) : 2.0;

    if (isNaN(r) || r < 2 || r > 5) {
        r = 2.0;
    }

    ctx.clearRect(0, 0, WIDTH, HEIGHT);

    drawArea(ctx, r);
    drawAxes(ctx, r);
    drawPoints(ctx, r);
}

function drawArea(ctx, r) {
    const scale = (WIDTH - 2 * PADDING) / (2 * r);

    ctx.fillStyle = 'rgba(100, 149, 237, 0.4)';

    // Прямоугольник в 1-й четверти (x: 0 до R/2, y: 0 до R)
    const rectWidth = (r / 2) * scale;
    const rectHeight = r * scale;
    ctx.fillRect(CENTER_X, CENTER_Y - rectHeight, rectWidth, rectHeight);

    // Четверть круга во 2-й четверти (радиус R/2)
    ctx.beginPath();
    ctx.arc(CENTER_X, CENTER_Y, (r / 2) * scale, Math.PI / 2, Math.PI);
    ctx.lineTo(CENTER_X, CENTER_Y);
    ctx.closePath();
    ctx.fill();

    // Треугольник в 4-й четверти
    ctx.beginPath();
    ctx.moveTo(CENTER_X, CENTER_Y);
    ctx.lineTo(CENTER_X + (r / 2) * scale, CENTER_Y);
    ctx.lineTo(CENTER_X, CENTER_Y + (r / 2) * scale);
    ctx.closePath();
    ctx.fill();
}

function drawAxes(ctx, r) {
    const scale = (WIDTH - 2 * PADDING) / (2 * r);

    ctx.strokeStyle = '#000';
    ctx.fillStyle = '#000';
    ctx.lineWidth = 2;
    ctx.font = '12px Arial';

    // Оси X и Y
    ctx.beginPath();
    ctx.moveTo(PADDING, CENTER_Y);
    ctx.lineTo(WIDTH - PADDING, CENTER_Y);
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(CENTER_X, PADDING);
    ctx.lineTo(CENTER_X, HEIGHT - PADDING);
    ctx.stroke();

    // Стрелки
    ctx.beginPath();
    ctx.moveTo(WIDTH - PADDING, CENTER_Y);
    ctx.lineTo(WIDTH - PADDING - 10, CENTER_Y - 5);
    ctx.lineTo(WIDTH - PADDING - 10, CENTER_Y + 5);
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(CENTER_X, PADDING);
    ctx.lineTo(CENTER_X - 5, PADDING + 10);
    ctx.lineTo(CENTER_X + 5, PADDING + 10);
    ctx.closePath();
    ctx.fill();

    // Подписи осей
    ctx.fillText('X', WIDTH - PADDING + 15, CENTER_Y + 5);
    ctx.fillText('Y', CENTER_X + 5, PADDING - 10);

    // Деления на осях
    ctx.lineWidth = 1;
    const marks = [-r, -r/2, r/2, r];
    const labels = ['-R', '-R/2', 'R/2', 'R'];

    for (let i = 0; i < marks.length; i++) {
        const x = CENTER_X + marks[i] * scale;
        const y = CENTER_Y - marks[i] * scale;

        // Деления по оси X
        ctx.beginPath();
        ctx.moveTo(x, CENTER_Y - 5);
        ctx.lineTo(x, CENTER_Y + 5);
        ctx.stroke();
        ctx.fillText(labels[i], x - 10, CENTER_Y + 20);

        // Деления по оси Y
        ctx.beginPath();
        ctx.moveTo(CENTER_X - 5, y);
        ctx.lineTo(CENTER_X + 5, y);
        ctx.stroke();
        ctx.fillText(labels[i], CENTER_X + 10, y + 5);
    }
}

function drawPoints(ctx, currentR) {
    const scale = (WIDTH - 2 * PADDING) / (2 * currentR);

    // Получить результаты из таблицы
    const table = document.querySelector('.results-table tbody');
    if (!table) return;

    const rows = table.querySelectorAll('tr');
    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        if (cells.length < 4) return;

        const x = parseFloat(cells[0].textContent);
        const y = parseFloat(cells[1].textContent);
        const hit = cells[3].textContent.includes('Попадание');

        const px = CENTER_X + x * scale;
        const py = CENTER_Y - y * scale;

        ctx.fillStyle = hit ? 'rgba(0, 200, 0, 0.7)' : 'rgba(200, 0, 0, 0.7)';
        ctx.beginPath();
        ctx.arc(px, py, 4, 0, 2 * Math.PI);
        ctx.fill();
    });
}

function handleCanvasClick(event, r) {
    const canvas = document.getElementById('graphCanvas');
    const rect = canvas.getBoundingClientRect();

    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;

    const scale = (WIDTH - 2 * PADDING) / (2 * r);

    const x = (clickX - CENTER_X) / scale;
    const y = (CENTER_Y - clickY) / scale;

    // Установить значения в скрытые поля
    const canvasXInput = document.querySelector('[id$="canvasX"]');
    const canvasYInput = document.querySelector('[id$="canvasY"]');

    if (canvasXInput && canvasYInput) {
        canvasXInput.value = x.toFixed(3);
        canvasYInput.value = y.toFixed(3);

        // Обновить значения в основной форме
        const xButtons = document.querySelector('[id$="xButtons"]');
        const yInput = document.querySelector('[id$="yInput"]');

        if (yInput) {
            yInput.value = y.toFixed(3);
        }

        // Отправить форму
        const submitButton = document.querySelector('[id$="canvasSubmit"]');
        if (submitButton) {
            submitButton.click();
        }
    }
}

// Перерисовать график при изменении радиуса
document.addEventListener('DOMContentLoaded', function() {
    const observer = new MutationObserver(function(mutations) {
        drawGraph();
    });

    const config = { childList: true, subtree: true };
    const targetNode = document.body;

    if (targetNode) {
        observer.observe(targetNode, config);
    }

    // Перерисовка при изменении слайдера
    setInterval(drawGraph, 500);
});
