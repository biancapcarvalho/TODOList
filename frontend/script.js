// simular saída do getAllTasks() do backend
let taskList = [
    {
        id: 1,
        title: "Tarefa 1",
        priority: 1,
        status: "todo",
        description: "Descrição da tarefa 1",
        category: "faculdade",
        dueDate: new Date(2026,5,10)
    },
    {
        id: 2,
        title: "Tarefa 2",
        priority: 2,
        status: "doing",
        description: "Descrição da tarefa 2",
        category: "casa",
        dueDate: new Date(2026,5,10)
    },
    {
        id: 3,
        title: "Tarefa 3",
        priority: 3,
        status: "done",
        description: "Descrição da tarefa 3",
        category: "trabalho",
        dueDate: new Date(2026,5,10)
    },
    {
        id: 4,
        title: "Tarefa 4",
        priority: 4,
        status: "todo",
        description: "Descrição da tarefa 4",
        category: "casa",
        dueDate: new Date(2026,5,10)
    },
    {
        id: 5,
        title: "Tarefa 5",
        priority: 5,
        status: "doing",
        description: "Descrição da tarefa 5",
        category: "faculdade",
        dueDate: new Date(2026,5,10)
    },
    {
        id: 6,
        title: "Tarefa 1",
        priority: 1,
        status: "todo",
        description: "Descrição da tarefa 1",
        category: "faculdade",
        dueDate: new Date(2026,5,10)
    },
    {
        id: 7,
        title: "Tarefa 2",
        priority: 2,
        status: "doing",
        description: "Descrição da tarefa 2",
        category: "casa",
        dueDate: new Date(2026,5,10)
    },
    {
        id: 8,
        title: "Tarefa 3",
        priority: 3,
        status: "done",
        description: "Descrição da tarefa 3",
        category: "trabalho",
        dueDate: new Date(2026,5,10)
    },
    {
        id: 9,
        title: "Tarefa 4",
        priority: 4,
        status: "todo",
        description: "Descrição da tarefa 4",
        category: "casa",
        dueDate: new Date(2026,5,10)
    },
    {
        id: 10,
        title: "Tarefa 5",
        priority: 5,
        status: "doing",
        description: "Descrição da tarefa 5",
        category: "faculdade",
        dueDate: new Date(2026,5,10)
    }
];

// adicionar colunas de status (sem as tarefas)
let status = {todo: "A FAZER", doing: "EM PROGRESSO", done: "CONCLUÍDO"};
let taskListDivInner = "";

for (const key of Object.keys(status)) {
    taskListDivInner += `
        <div class="status-column">
            <div class="column-title">
                <p>${status[key]}</p>
                <i class="fa-regular fa-plus"></i>
            </div>
            <!-- todo - um laço para adicionar os cards de tarefa aqui -->
            <div id="${key}-column">
            </div>
        </div>
    `
}

document.getElementById('task-list-div').innerHTML = taskListDivInner;

// adicionar as tarefas nas colunas
let todoTasks = "";
let doingTasks = "";
let doneTasks = "";
for (const task of taskList) {
    if (task.status === "todo") {
        todoTasks += `
            <div class="task-mini-card">
                <div class="header-task">
                    <p>${task.title}</p>
                    <div>
                        <i class="fa-regular fa-pen-to-square"></i>
                        <i class="fa-solid fa-trash"></i>
                    </div>
                </div>
                <div class="footer-task">
                    <div>
                        <p class="due-date">
                            <i class="fa-regular fa-clock"></i>
                            ${task.dueDate.toLocaleDateString()}
                        </p>
                        <p class="category">${task.category}</p>
                    </div>
                    <p class="priority-${task.priority}">
                        <i></i>
                    </p>
                </div>
            </div>
        `
    }

    if (task.status === "doing") {
        doingTasks += `
            <div class="task-mini-card">
                <div class="header-task">
                    <p>${task.title}</p>
                    <div>
                        <i class="fa-regular fa-pen-to-square"></i>
                        <i class="fa-solid fa-trash"></i>
                    </div>
                </div>
                <div class="footer-task">
                    <div>
                        <p class="due-date">
                            <i class="fa-regular fa-clock"></i>
                            ${task.dueDate.toLocaleDateString()}
                        </p>
                        <p class="category">${task.category}</p>
                    </div>
                    <p class="priority-${task.priority}">
                        <i></i>
                    </p>
                </div>
            </div>
        `
    }

    if (task.status === "done") {
        doneTasks += `
            <div class="task-mini-card">
                <div class="header-task">
                    <p>${task.title}</p>
                    <div>
                        <i class="fa-regular fa-pen-to-square"></i>
                        <i class="fa-solid fa-trash"></i>
                    </div>
                </div>
                <div class="footer-task">
                    <div>
                        <p class="due-date">
                            <i class="fa-regular fa-clock"></i>
                            ${task.dueDate.toLocaleDateString()}
                        </p>
                        <p class="category">${task.category}</p>
                    </div>
                    <p class="priority-${task.priority}">
                        <i></i>
                    </p>
                </div>
            </div>
        `
    }
}

document.getElementById("todo-column").innerHTML = todoTasks;
document.getElementById("doing-column").innerHTML = doingTasks;
document.getElementById("done-column").innerHTML = doneTasks;

// setando os icones de prioridade
let priority1Elements = document.getElementsByClassName("priority-1")
for (const el of priority1Elements) {
    el.getElementsByTagName("i")[0].setAttribute("class", "fa-solid fa-angles-down");
    el.getElementsByTagName("i")[0].style.color = "dodgerblue";
}

let priority2Elements = document.getElementsByClassName("priority-2")
for (const el of priority2Elements) {
    el.getElementsByTagName("i")[0].setAttribute("class", "fa-solid fa-angle-down");
    el.getElementsByTagName("i")[0].style.color = "dodgerblue";
}

let priority3Elements = document.getElementsByClassName("priority-3")
for (const el of priority3Elements) {
    el.getElementsByTagName("i")[0].setAttribute("class", "fa-solid fa-equals");
    el.getElementsByTagName("i")[0].style.color = "darkorange";
}

let priority4Elements = document.getElementsByClassName("priority-4")
for (const el of priority4Elements) {
    el.getElementsByTagName("i")[0].setAttribute("class", "fa-solid fa-angle-up");
    el.getElementsByTagName("i")[0].style.color = "darkred";
}

let priority5Elements = document.getElementsByClassName("priority-5")
for (const el of priority5Elements) {
    el.getElementsByTagName("i")[0].setAttribute("class", "fa-solid fa-angles-up");
    el.getElementsByTagName("i")[0].style.color = "darkred";
}