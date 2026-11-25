// API URLs
const API_PROJECTS = "http://localhost:8080/api/projects";
const API_TASKS = "http://localhost:8080/api/tasks";
const API_STATS = "http://localhost:8080/api/stats";
const API_REMINDERS = "http://localhost:8080/api/reminders";

let allProjectsData = [];

document.addEventListener("DOMContentLoaded", () => {
    loadAllData();
    
    document.getElementById("projectForm").addEventListener("submit", addProject);
    document.getElementById("taskForm").addEventListener("submit", addTask);

    setInterval(loadReminders, 30000);
});

function loadAllData() {
    loadStats();
    loadProjects();
    loadTasks();
    loadReminders();
}

function loadStats() {
    fetch(API_STATS)
        .then(res => res.json())
        .then(data => {
            document.getElementById("totalProjects").innerText = data.totalProjects;
            document.getElementById("totalTasks").innerText = data.totalTasks;
            document.getElementById("completedTasks").innerText = data.completedTasks;
            document.getElementById("pendingTasks").innerText = data.pendingTasks;
        })
        .catch(err => console.log("Error loading stats:", err));
}

function loadReminders() {
    fetch(API_REMINDERS)
        .then(res => res.json())
        .then(data => {
            const reminderBox = document.getElementById("reminderSection");
            const list = document.getElementById("reminderList");
            
            if (data.length > 0) {
                reminderBox.style.display = "block";
                list.innerHTML = "";
                data.forEach(task => {
                    list.innerHTML += `<li>🚨 Task: <b>${task.title}</b> was due on ${task.dueDate}</li>`;
                });
            } else {
                reminderBox.style.display = "none";
            }
        })
        .catch(err => console.log("Error loading reminders:", err));
}

function loadProjects() {
    fetch(API_PROJECTS)
        .then(res => res.json())
        .then(data => {
            allProjectsData = data;
            const tbody = document.getElementById("projectTableBody");
            tbody.innerHTML = "";
            
            data.forEach(p => {
                let statusDropdown = `
                    <select class="status-select status-${p.status}" onchange="updateProjectStatus(${p.id}, this.value)">
                        <option value="TODO" ${p.status === 'TODO' ? 'selected' : ''}>TODO</option>
                        <option value="IN_PROGRESS" ${p.status === 'IN_PROGRESS' ? 'selected' : ''}>IN PROGRESS</option>
                        <option value="DONE" ${p.status === 'DONE' ? 'selected' : ''}>DONE</option>
                    </select>
                `;

                tbody.innerHTML += `
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.name}</td>
                        <td>${statusDropdown}</td>
                        <td>${p.dueDate}</td>
                        <td>
                            <button class="btn-delete" onclick="deleteProject(${p.id})">Delete</button>
                        </td>
                    </tr>
                `;
            });
        });
}

function loadTasks() {
    fetch(API_TASKS)
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("taskTableBody");
            tbody.innerHTML = "";
            data.forEach(t => {
                let statusDropdown = `
                    <select class="status-select status-${t.status}" onchange="updateTaskStatus(${t.id}, this.value)">
                        <option value="TODO" ${t.status === 'TODO' ? 'selected' : ''}>TODO</option>
                        <option value="IN_PROGRESS" ${t.status === 'IN_PROGRESS' ? 'selected' : ''}>IN PROGRESS</option>
                        <option value="DONE" ${t.status === 'DONE' ? 'selected' : ''}>DONE</option>
                    </select>
                `;

                tbody.innerHTML += `
                    <tr>
                        <td>${t.id}</td>
                        <td>${t.title}</td>
                        <td>${t.priority}</td>
                        <td>${statusDropdown}</td>
                        <td>
                            <button class="btn-delete" onclick="deleteTask(${t.id})">Delete</button>
                        </td>
                    </tr>
                `;
            });
        });
}

function addProject(event) {
    event.preventDefault();
    const formData = new URLSearchParams();
    formData.append("name", document.getElementById("pName").value);
    formData.append("description", document.getElementById("pDesc").value);
    formData.append("startDate", document.getElementById("pStart").value);
    formData.append("dueDate", document.getElementById("pDue").value);

    fetch(API_PROJECTS, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) alert("Error: " + data.error);
        else {
            alert("✅ Project Added!");
            document.getElementById("projectForm").reset();
            loadAllData(); 
        }
    });
}

function addTask(event) {
    event.preventDefault();
    const formData = new URLSearchParams();
    formData.append("projectId", document.getElementById("tProjectId").value);
    formData.append("title", document.getElementById("tTitle").value);
    formData.append("description", document.getElementById("tDesc").value);
    formData.append("priority", document.getElementById("tPriority").value);
    formData.append("dueDate", document.getElementById("tDue").value);

    fetch(API_TASKS, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) alert("Error: " + data.error);
        else {
            alert("✅ Task Added!");
            document.getElementById("taskForm").reset();
            loadAllData(); 
        }
    });
}

function updateProjectStatus(id, newStatus) {
    const project = allProjectsData.find(p => p.id === id);
    if (!project) return;

    const formData = new URLSearchParams();
    formData.append("id", id);
    formData.append("name", project.name);
    formData.append("description", project.description);
    formData.append("startDate", project.startDate);
    formData.append("dueDate", project.dueDate);
    formData.append("status", newStatus);

    fetch(API_PROJECTS, {
        method: "PUT",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) alert("Error: " + data.error);
        loadAllData(); // ALWAYS REFRESH
    });
}

function updateTaskStatus(id, newStatus) {
    const formData = new URLSearchParams();
    formData.append("id", id);
    formData.append("status", newStatus);

    fetch(API_TASKS, {
        method: "PUT",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) alert("Error: " + data.error);
        loadAllData(); // ALWAYS REFRESH
    });
}

function deleteProject(id) {
    if (confirm("Are you sure you want to delete this project? All its tasks will also be deleted.")) {
        fetch(`${API_PROJECTS}?id=${id}`, { method: "DELETE" })
            .then(res => res.json())
            .then(data => {
                if (data.error) alert("Error: " + data.error);
                else { alert("🗑️ " + data.message); loadAllData(); }
            });
    }
}

function deleteTask(id) {
    if (confirm("Are you sure you want to delete this task?")) {
        fetch(`${API_TASKS}?id=${id}`, { method: "DELETE" })
            .then(res => res.json())
            .then(data => {
                if (data.error) alert("Error: " + data.error);
                else { alert("🗑️ " + data.message); loadAllData(); }
            });
    }
}