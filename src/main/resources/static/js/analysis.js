const scoreChart = echarts.init(document.getElementById('scoreChart'));
const muscleChart = echarts.init(document.getElementById('muscleChart'));

function loadAnalysis() {
    const athleteId = document.getElementById('athleteIdInput').value.trim();
    if (!athleteId) {
        alert('请输入运动员 ID');
        return;
    }

    // 按你的后端接口改这里
    fetch(`/analysis/${athleteId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('接口请求失败');
            }
            return response.json();
        })
        .then(data => {
            renderStats(data);
            renderScoreChart(data.records || []);
            renderMuscleChart(data.muscleStats || {});
            renderTable(data.records || []);
        })
        .catch(error => {
            console.error(error);
            alert('加载分析数据失败，请检查接口地址或后端服务');
        });
}

function renderStats(data) {
    document.getElementById('totalCount').innerText = data.totalCount ?? 0;
    document.getElementById('avgScore').innerText = Number(data.avgScore ?? 0).toFixed(1);
    document.getElementById('bestScore').innerText = data.bestScore ?? 0;
    document.getElementById('tenRate').innerText = Number(data.tenRate ?? 0).toFixed(1) + '%';
}

function renderScoreChart(records) {
    const list = [...records].reverse();
    const xData = list.map((item, index) => {
        if (item.createdAt) {
            return item.createdAt.replace('T', ' ').substring(5, 16);
        }
        return '记录' + (index + 1);
    });
    const yData = list.map(item => item.score ?? 0);

    scoreChart.setOption({
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            left: '5%',
            right: '5%',
            top: '12%',
            bottom: '8%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: xData,
            boundaryGap: false,
            axisLine: {
                lineStyle: {
                    color: '#cbd5e1'
                }
            },
            axisLabel: {
                color: '#64748b',
                rotate: 25
            }
        },
        yAxis: {
            type: 'value',
            axisLine: {
                show: false
            },
            splitLine: {
                lineStyle: {
                    color: '#e2e8f0'
                }
            },
            axisLabel: {
                color: '#64748b'
            }
        },
        series: [{
            name: '成绩',
            type: 'line',
            smooth: true,
            data: yData,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: {
                width: 4
            },
            areaStyle: {
                opacity: 0.15
            }
        }]
    });
}

function renderMuscleChart(muscleStats) {
    const data = Object.keys(muscleStats).map(key => ({
        name: key,
        value: muscleStats[key]
    }));

    muscleChart.setOption({
        tooltip: {
            trigger: 'item'
        },
        legend: {
            bottom: 0,
            textStyle: {
                color: '#64748b'
            }
        },
        series: [{
            name: '肌肉状态',
            type: 'pie',
            radius: ['42%', '70%'],
            center: ['50%', '45%'],
            avoidLabelOverlap: false,
            itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 3
            },
            label: {
                show: true,
                formatter: '{b}\n{d}%'
            },
            data: data
        }]
    });
}

function renderTable(records) {
    const tbody = document.getElementById('recordTableBody');

    if (!records || records.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="4" class="empty-box">暂无训练记录</td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = records.map((item, index) => `
        <tr>
            <td>${index + 1}</td>
            <td><span class="tag">${item.score ?? 0}</span></td>
            <td>${item.muscleStatus ?? '-'}</td>
            <td>${formatDateTime(item.createdAt)}</td>
        </tr>
    `).join('');
}

function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return '-';
    return dateTimeStr.replace('T', ' ').substring(0, 19);
}

window.addEventListener('resize', function () {
    scoreChart.resize();
    muscleChart.resize();
});