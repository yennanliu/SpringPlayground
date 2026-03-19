// API Base URL
const API_BASE = '/api/v1';

// State
let nodes = [];
let clusterStatus = null;
let clusterHealth = null;

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    refreshData();
    // Auto-refresh every 30 seconds
    setInterval(refreshData, 30000);
});

// Fetch all data
async function refreshData() {
    await Promise.all([
        fetchClusterStatus(),
        fetchClusterHealth(),
        fetchNodes()
    ]);
}

// Fetch cluster status
async function fetchClusterStatus() {
    try {
        const response = await fetch(`${API_BASE}/cluster/status`);
        clusterStatus = await response.json();
        updateStatusCards();
    } catch (error) {
        console.error('Error fetching cluster status:', error);
        showToast('Failed to fetch cluster status', 'error');
    }
}

// Fetch cluster health
async function fetchClusterHealth() {
    try {
        const response = await fetch(`${API_BASE}/cluster/health`);
        clusterHealth = await response.json();
        updateHealthDisplay();
        updateUnhealthySection();
    } catch (error) {
        console.error('Error fetching cluster health:', error);
    }
}

// Fetch nodes
async function fetchNodes() {
    try {
        const response = await fetch(`${API_BASE}/nodes`);
        nodes = await response.json();
        renderNodesTable();
    } catch (error) {
        console.error('Error fetching nodes:', error);
        showToast('Failed to fetch nodes', 'error');
    }
}

// Update status cards
function updateStatusCards() {
    if (!clusterStatus) return;

    document.getElementById('total-nodes').textContent = clusterStatus.totalNodes || 0;
    document.getElementById('running-nodes').textContent = clusterStatus.runningNodes || 0;
    document.getElementById('pending-nodes').textContent = clusterStatus.pendingNodes || 0;
    document.getElementById('stopped-nodes').textContent = clusterStatus.stoppedNodes || 0;
    document.getElementById('unhealthy-nodes').textContent = clusterStatus.unhealthyNodes || 0;

    // Update metrics
    const avgCpu = clusterStatus.averageCpuUsage;
    const avgMem = clusterStatus.averageMemoryUsage;
    document.getElementById('avg-cpu').textContent = avgCpu !== null ? `${avgCpu}%` : 'N/A';
    document.getElementById('avg-memory').textContent = avgMem !== null ? `${avgMem}%` : 'N/A';

    // Update last updated time
    if (clusterStatus.lastUpdated) {
        const date = new Date(clusterStatus.lastUpdated);
        document.getElementById('last-updated').textContent = date.toLocaleTimeString();
    }
}

// Update health display
function updateHealthDisplay() {
    if (!clusterHealth) return;

    const healthStatus = document.getElementById('health-status');
    healthStatus.textContent = clusterHealth.status || 'Unknown';
    healthStatus.className = `health-status ${clusterHealth.status || ''}`;
}

// Update unhealthy section
function updateUnhealthySection() {
    const section = document.getElementById('unhealthy-section');
    const list = document.getElementById('unhealthy-list');

    if (!clusterHealth || !clusterHealth.unhealthyNodesList || clusterHealth.unhealthyNodesList.length === 0) {
        section.style.display = 'none';
        return;
    }

    section.style.display = 'block';
    list.innerHTML = clusterHealth.unhealthyNodesList.map(node => `
        <div class="unhealthy-item">
            <div class="unhealthy-info">
                <h4>${escapeHtml(node.name)}</h4>
                <p>Instance: ${node.instanceId || 'N/A'} | Failed Checks: ${node.failedChecks}</p>
            </div>
            <button class="btn btn-sm btn-success" onclick="startNode('${node.id}')">
                Restart
            </button>
        </div>
    `).join('');
}

// Render nodes table
function renderNodesTable() {
    const tbody = document.getElementById('nodes-tbody');

    if (nodes.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="9">
                    <div class="empty-state">
                        <h3>No nodes yet</h3>
                        <p>Create your first node to get started</p>
                    </div>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = nodes.map(node => `
        <tr>
            <td>
                <strong>${escapeHtml(node.name)}</strong>
            </td>
            <td>${node.instanceId || '<span style="color: var(--gray-400)">Not assigned</span>'}</td>
            <td>
                <span class="status-badge ${node.status}">${node.status}</span>
            </td>
            <td>${node.instanceType || 'N/A'}</td>
            <td>${node.privateIp || 'N/A'}</td>
            <td>${node.cpuUsage !== null ? node.cpuUsage + '%' : 'N/A'}</td>
            <td>${node.memoryUsage !== null ? node.memoryUsage + '%' : 'N/A'}</td>
            <td>${formatTimestamp(node.lastHeartbeat)}</td>
            <td class="actions-cell">
                ${getNodeActions(node)}
            </td>
        </tr>
    `).join('');
}

// Get action buttons for a node
function getNodeActions(node) {
    const actions = [];

    actions.push(`<button class="btn btn-sm btn-secondary" onclick="viewNodeDetails('${node.id}')">View</button>`);

    if (node.status === 'STOPPED' || node.status === 'PENDING') {
        actions.push(`<button class="btn btn-sm btn-success" onclick="startNode('${node.id}')">Start</button>`);
    }

    if (node.status === 'RUNNING' || node.status === 'UNHEALTHY') {
        actions.push(`<button class="btn btn-sm btn-warning" onclick="stopNode('${node.id}')">Stop</button>`);
    }

    if (node.status !== 'TERMINATED') {
        actions.push(`<button class="btn btn-sm btn-danger" onclick="terminateNode('${node.id}')">Terminate</button>`);
    }

    return actions.join('');
}

// Format timestamp
function formatTimestamp(timestamp) {
    if (!timestamp) return '<span style="color: var(--gray-400)">Never</span>';
    const date = new Date(timestamp);
    const now = new Date();
    const diff = Math.floor((now - date) / 1000);

    if (diff < 60) return `${diff}s ago`;
    if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
    return date.toLocaleDateString();
}

// Escape HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Create Modal
function openCreateModal() {
    document.getElementById('create-modal').classList.add('active');
    document.getElementById('node-name').focus();
}

function closeCreateModal() {
    document.getElementById('create-modal').classList.remove('active');
    document.getElementById('create-form').reset();
}

// Create node
async function createNode(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const data = {
        name: formData.get('name'),
        instanceType: formData.get('instanceType'),
        availabilityZone: formData.get('availabilityZone')
    };

    try {
        const response = await fetch(`${API_BASE}/nodes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to create node');
        }

        showToast('Node created successfully', 'success');
        closeCreateModal();
        refreshData();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// View node details
async function viewNodeDetails(nodeId) {
    try {
        const response = await fetch(`${API_BASE}/nodes/${nodeId}`);
        if (!response.ok) throw new Error('Node not found');

        const node = await response.json();
        showNodeDetailsModal(node);
    } catch (error) {
        showToast(error.message, 'error');
    }
}

function showNodeDetailsModal(node) {
    const content = document.getElementById('node-details-content');
    content.innerHTML = `
        <div class="detail-grid">
            <div class="detail-item">
                <label>Node ID</label>
                <span>${node.id}</span>
            </div>
            <div class="detail-item">
                <label>Name</label>
                <span>${escapeHtml(node.name)}</span>
            </div>
            <div class="detail-item">
                <label>Instance ID</label>
                <span>${node.instanceId || 'Not assigned'}</span>
            </div>
            <div class="detail-item">
                <label>Status</label>
                <span class="status-badge ${node.status}">${node.status}</span>
            </div>
            <div class="detail-item">
                <label>Instance Type</label>
                <span>${node.instanceType || 'N/A'}</span>
            </div>
            <div class="detail-item">
                <label>Availability Zone</label>
                <span>${node.availabilityZone || 'N/A'}</span>
            </div>
            <div class="detail-item">
                <label>Private IP</label>
                <span>${node.privateIp || 'N/A'}</span>
            </div>
            <div class="detail-item">
                <label>Public IP</label>
                <span>${node.publicIp || 'N/A'}</span>
            </div>
            <div class="detail-item">
                <label>CPU Usage</label>
                <span>${node.cpuUsage !== null ? node.cpuUsage + '%' : 'N/A'}</span>
            </div>
            <div class="detail-item">
                <label>Memory Usage</label>
                <span>${node.memoryUsage !== null ? node.memoryUsage + '%' : 'N/A'}</span>
            </div>
            <div class="detail-item">
                <label>Last Heartbeat</label>
                <span>${node.lastHeartbeat ? new Date(node.lastHeartbeat).toLocaleString() : 'Never'}</span>
            </div>
            <div class="detail-item">
                <label>Created At</label>
                <span>${node.createdAt ? new Date(node.createdAt).toLocaleString() : 'N/A'}</span>
            </div>
        </div>
        <div class="detail-actions">
            ${node.status !== 'RUNNING' && node.status !== 'TERMINATED' ? `
                <button class="btn btn-success" onclick="startNode('${node.id}'); closeDetailsModal();">Start</button>
            ` : ''}
            ${node.status === 'RUNNING' || node.status === 'UNHEALTHY' ? `
                <button class="btn btn-warning" onclick="stopNode('${node.id}'); closeDetailsModal();">Stop</button>
            ` : ''}
            ${node.status !== 'TERMINATED' ? `
                <button class="btn btn-danger" onclick="terminateNode('${node.id}'); closeDetailsModal();">Terminate</button>
            ` : ''}
            <button class="btn btn-secondary" onclick="syncNode('${node.id}')">Sync with EC2</button>
            <button class="btn btn-danger" onclick="deleteNode('${node.id}'); closeDetailsModal();">Delete</button>
        </div>
    `;

    document.getElementById('details-modal').classList.add('active');
}

function closeDetailsModal() {
    document.getElementById('details-modal').classList.remove('active');
}

// Node actions
async function startNode(nodeId) {
    if (!confirm('Are you sure you want to start this node?')) return;

    try {
        const response = await fetch(`${API_BASE}/nodes/${nodeId}/start`, { method: 'POST' });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to start node');
        }
        showToast('Node start initiated', 'success');
        refreshData();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function stopNode(nodeId) {
    if (!confirm('Are you sure you want to stop this node?')) return;

    try {
        const response = await fetch(`${API_BASE}/nodes/${nodeId}/stop`, { method: 'POST' });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to stop node');
        }
        showToast('Node stop initiated', 'success');
        refreshData();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function terminateNode(nodeId) {
    if (!confirm('Are you sure you want to TERMINATE this node? This action cannot be undone.')) return;

    try {
        const response = await fetch(`${API_BASE}/nodes/${nodeId}/terminate`, { method: 'POST' });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to terminate node');
        }
        showToast('Node terminated', 'success');
        refreshData();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function deleteNode(nodeId) {
    if (!confirm('Are you sure you want to DELETE this node from the cluster?')) return;

    try {
        const response = await fetch(`${API_BASE}/nodes/${nodeId}`, { method: 'DELETE' });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to delete node');
        }
        showToast('Node deleted', 'success');
        refreshData();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function syncNode(nodeId) {
    try {
        const response = await fetch(`${API_BASE}/nodes/${nodeId}/sync`, { method: 'POST' });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to sync node');
        }
        showToast('Node synced', 'success');
        viewNodeDetails(nodeId);
        refreshData();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Cluster actions
async function syncCluster() {
    try {
        const response = await fetch(`${API_BASE}/cluster/sync`, { method: 'POST' });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to sync cluster');
        }
        showToast('Cluster sync completed', 'success');
        refreshData();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function triggerHealthCheck() {
    try {
        const response = await fetch(`${API_BASE}/cluster/health-check`, { method: 'POST' });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to trigger health check');
        }
        showToast('Health check completed', 'success');
        refreshData();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Toast notification
function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} show`;

    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Close modals on escape key
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        closeCreateModal();
        closeDetailsModal();
    }
});

// Close modals on outside click
document.getElementById('create-modal').addEventListener('click', (e) => {
    if (e.target.id === 'create-modal') closeCreateModal();
});

document.getElementById('details-modal').addEventListener('click', (e) => {
    if (e.target.id === 'details-modal') closeDetailsModal();
});
