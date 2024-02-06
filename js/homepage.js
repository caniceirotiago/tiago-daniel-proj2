/* JavaScript File - all the code that was written goes here  */
/* Switch to strict mode to get more useful errors, when/if you make mistakes. */
'use strict';


import * as language from "./language.js";
import * as username from "./username.js";
import * as theme from "./theme.js";

language.listenerLanguageBtns(); // adds listener to the language buttons
/**************************************************************************************************************************************************************************************/ 
/* DOMcl sets username, changes theme *** */
/**************************************************************************************************************************************************************************************/ 
document.addEventListener('DOMContentLoaded', function() {
    username.setUsername(); // set username on loading
    theme.loadTheme(); // loads up the previously set theme
    language.underlineLangFlag();
    createDropListnerForTasks();
    loadTasks();   
    saveTasks(); //necessita de gravar  
    updateTaskCountView();
    clickOnTaskListner();
});

/**************************************************************************************************************************************************************************************/ 
/* function loadTasks - LOAD ALL TASKS */
/**************************************************************************************************************************************************************************************/
function loadTasks() {
    const tasks = JSON.parse(localStorage.getItem('tasks')) || [];// vai buscar as tarefas gravadas anteriormente
    tasks.forEach(task => {
        addTaskToRightList(task); // para cada terefa chama ométodo para a adicionar à lista correta
    });
};
/**************************************************************************************************************************************************************************************/ 
/* function addTaskToRightList - ADD TASKS TO THE RIGHT LIST */ 
/**************************************************************************************************************************************************************************************/
function addTaskToRightList(task) {
    /* <li> list items */
    const itemList = document.createElement('li');// Creates a new <li> element
    itemList.setAttribute('data-task-id', task.id); 
    itemList.classList.add('task-item');
    itemList.setAttribute('draggable','true');
    const itemTitle = document.createElement('h3');
    itemTitle.textContent = task.title;
    const itemDescription = document.createElement('p');
    itemDescription.textContent = task.description;
    
    /* Creating the buttons */
    const nextButton = createNextButton();
    const delButton = createDelButton();
    const prevButton = createPrevButton();

    /* Creating the button Listeners */
    createNextBtnListener(nextButton, task);
    createDelBtnListener(delButton, task);
    createPrevBtnListener(prevButton, task);
    createDragDropListener(itemList, task);

    /* Creating div's */
    const bannerDiv = document.createElement('div');
    bannerDiv.classList.add("banner");
    bannerDiv.appendChild(itemTitle);
    const contentDiv = document.createElement('div');
    contentDiv.classList.add("content");
    contentDiv.appendChild(itemDescription);

    /* Append Title and Description to Task */
    itemList.appendChild(bannerDiv);
    itemList.appendChild(contentDiv);
    
    /* Append Buttons to Task - with contextual relevance logic */
    if (task.status !== 'done') { itemList.appendChild(nextButton); } //this one is not added in right most column
    itemList.appendChild(delButton); // this one is always added
    if (task.status !== 'todo') { itemList.appendChild(prevButton); } //this one is not added in left most column
    
    /* Add Task to correct List */
    document.getElementById(task.status).appendChild(itemList);
    updateTaskCountView();
};
/**************************************************************************************************************************************************************************************/ 
/* function createNextButton() - creates and returns the nextButton  */
/**************************************************************************************************************************************************************************************/
function createNextButton() {
    const nextButton = document.createElement('button');
    nextButton.textContent = '>';

    return nextButton;
};
/**************************************************************************************************************************************************************************************/ 
/* function createDelButton() - creates and returns the delButton  */
/**************************************************************************************************************************************************************************************/
function createDelButton() {
    const delButton = document.createElement('button');
    const delIcon = document.createElement('img');
    delIcon.src = "images/trashCanIcon.png";
    delIcon.alt = 'del';
    delButton.appendChild(delIcon);
    
    return delButton;
};
/**************************************************************************************************************************************************************************************/ 
/* function createPrevButton() - creates and returns the prevButton  */
/**************************************************************************************************************************************************************************************/
function createPrevButton() {
    const prevButton = document.createElement('button');
    prevButton.textContent = '<';

    return prevButton;
};
/**************************************************************************************************************************************************************************************/ 
/* function createDragDropListener --- 'e',aka, event object -> `dataTransfer` property -> sets the data, of the element being dragged, as the `id` of the `task` object
/**************************************************************************************************************************************************************************************/
function createDragDropListener(itemList, task){
    itemList.addEventListener('dragstart', function(e) {
        e.dataTransfer.setData('text/plain', task.id);
    });
};
function createDropListnerForTasks(){
    document.querySelectorAll(".ul-tasks").forEach(column => { //faz com que as listas recebam itens
    const status = column.id;
    column.addEventListener('dragover', function(e) {
        e.preventDefault(); // Permite o drop
    });

    column.addEventListener('drop', function(e) {
        e.preventDefault();
        const taskId = e.dataTransfer.getData('text/plain');
        // Lógica para mover a tarefa para a coluna atual
        moveTaskToColumnOnDragDrop(taskId, status);
    });
});
}
/**************************************************************************************************************************************************************************************/ 
/* function moveTaskToColumnOnDragDrop - handles movint a task to another collumn on drag and drop*/
/**************************************************************************************************************************************************************************************/
function moveTaskToColumnOnDragDrop(taskId, newStatus){
    let tasks = JSON.parse(localStorage.getItem('tasks')) || [];
    let task = tasks.find(t => t.id === taskId);
    if (task) {
         // Atualizar o status da tarefa
        task.status = newStatus;
         // Atualizar as tarefas no armazenamento local
        localStorage.setItem('tasks', JSON.stringify(tasks));
         // Mover a representação visual da tarefa para a coluna correta
        moveTaskElementOnDropVisualy(task);
    }
};
/**************************************************************************************************************************************************************************************/ 
/* function moveTaskElementOnDropVisualy(task) ---- handles movint a task to another collumn on drag and drop visual*/
/**************************************************************************************************************************************************************************************/
function moveTaskElementOnDropVisualy(task) {
    // Remover a tarefa da sua coluna atual
    const existingElement = document.querySelector(`[data-task-id="${task.id}"]`);
    if (existingElement) {
        existingElement.remove();
    }

    // Adicionar a tarefa à nova coluna
    addTaskToRightList(task);
};
/**************************************************************************************************************************************************************************************/ 
/* ADD ACTION LISTENERS TO THE EACH TASK ITEM - Only on the task-item excluding buttons 
/**************************************************************************************************************************************************************************************/
function clickOnTaskListner(){
      const tasksContainer = document.querySelector('.mainBoard-tasks-container');

    tasksContainer.addEventListener('click', function(event) {
        // Verificar se o clique foi diretamente num botão
        if (event.target.tagName === 'BUTTON'|| event.target.tagName === 'IMG') {
            return; // Não faz nada se um botão foi clicado, permitindo que o evento do botão seja processado
        }

        let targetElement = event.target;
        //verifica qual o elemento pai que realmente corresponde ao um task-item
        while (targetElement != null && !targetElement.classList.contains('task-item')) {
            targetElement = targetElement.parentElement;
        }
        // Se um task-item for clicado
        if (targetElement && targetElement.classList.contains('task-item')) {
            
            const taskId = targetElement.getAttribute('data-task-id');
            window.location.href = `edittask.html?taskId=${taskId}`;
        }
    });
}
/**************************************************************************************************************************************************************************************/ 
/* function createNextBtnListener - CREATES NEXT BUTTON LISTENER AND HANDLES THE LOGIC RESPONSE - moving to NEXT column and saving/updating the display
/**************************************************************************************************************************************************************************************/
function createNextBtnListener(nextButton, task) {
    nextButton.addEventListener('click', function() {
        let nextStatus =""; // declare variable: var nextStatus is not recommended after IE6, best practice is let keyword
        if (task.status === 'todo') {
            nextStatus = 'doing';
        } else if (task.status === 'doing') {
            nextStatus = 'done';
        }
        else if (task.status === 'done') {
            nextStatus = 'done';
        }
        moveTaskOnCLick(task, nextStatus);
    });
};
/**************************************************************************************************************************************************************************************/ 
/* function createDelBtnListener - CREATES DEL BUTTON LISTENER AND HANDLES THE LOGIC RESPONSE - deleting the task if pressed + confirmed
/**************************************************************************************************************************************************************************************/
function createDelBtnListener(delButton, task) {
    delButton.addEventListener('click', function() {
        if (delConfirmation()) { // boolean confirm
            delTask(task);
        }
    });
};
/**************************************************************************************************************************************************************************************/ 
/* function delConfirmation - Delete confirmation small box appears - boolean logic return value
/**************************************************************************************************************************************************************************************/
function delConfirmation(){
    let delConfirmMsg = 'Are you sure you want to delete this task?';
    // (alternatives would be: alert ||prompt || modal popup (but those are annoying! please never use those))
    return confirm(delConfirmMsg);
};
/**************************************************************************************************************************************************************************************/ 
/* function createPrevBtnListener - CREATES PREV BUTTON LISTENER AND HANDLES THE LOGIC RESPONSE - moving to PREVIOUS column and saving/updating the display
/**************************************************************************************************************************************************************************************/
function createPrevBtnListener(nextButton, task) {
    nextButton.addEventListener('click', function() {
        let nextStatus ="";
        if (task.status === 'doing') {
            nextStatus = 'todo';
        } else if (task.status === 'done') {
            nextStatus = 'doing';
        }
        else if (task.status === 'todo') {
            nextStatus = 'todo';
        }
        moveTaskOnCLick(task, nextStatus);
    });
};
/**************************************************************************************************************************************************************************************/ 
/* function delTask(task) - DELETES A TASK PASSED BY ARGUMENT - deletes task and saves/updatesthe display
/**************************************************************************************************************************************************************************************/
function delTask(task) {
    const oldTaskElement = document.querySelector(`[data-task-id="${task.id}"]`);
    if (oldTaskElement) {
        oldTaskElement.remove();
    }
    saveTasks(); // Saves Tasks, thus also updating the localStorage
    updateTaskCountView();
};
/**************************************************************************************************************************************************************************************/ 
/* function moveTaskOnCLick(task, nextStatus) - 
/**************************************************************************************************************************************************************************************/
function moveTaskOnCLick(task, nextStatus) {
    const oldTaskElement = document.querySelector(`[data-task-id="${task.id}"]`);
    if (oldTaskElement) {
        oldTaskElement.remove();
    }

    // Cria uma nova tarefa atualizada
    const updatedTask = {...task, status: nextStatus};
    addTaskToRightList(updatedTask);

    // Salva a tarefa
    saveTasks();
}
/**************************************************************************************************************************************************************************************/ 
/* function saveTasks() 
/**************************************************************************************************************************************************************************************/
function saveTasks() {
    const tasks = [];
    ['todo', 'doing', 'done'].forEach(status => {
        document.querySelectorAll('#' + status + ' .task-item').forEach(taskElement => {
            const taskTitle = taskElement.querySelector('h3').textContent;
            const taskDescription = taskElement.querySelector('p').textContent;
            const taskId = taskElement.dataset.taskId;
            tasks.push({ 
                id: taskId, 
                title: taskTitle, 
                description: taskDescription, 
                status: status
            });
        });
    });
    localStorage.setItem('tasks', JSON.stringify(tasks));
}
/**************************************************************************************************************************************************************************************/
/* function countTODOTasks() --- /*Contagem de tarefas da COLUNA TODO */
/**************************************************************************************************************************************************************************************/
function countTODOTasks(){
    const taskList = document.getElementById("todo");
    let nOfTasks = taskList.childElementCount;
    return nOfTasks;
};
/**************************************************************************************************************************************************************************************/
/* function countDOINGTasks() --- /*Contagem de tarefas da COLUNA DOING */
/**************************************************************************************************************************************************************************************/
function countDOINGTasks(){
    const taskList = document.getElementById("doing");
    let nOfTasks = taskList.childElementCount;
    return nOfTasks;
};
/**************************************************************************************************************************************************************************************/
/* function countDOINGTasks() --- /*Contagem de tarefas da COLUNA DONE */
/**************************************************************************************************************************************************************************************/
function countDONETasks(){
    const taskList = document.getElementById("done");
    let nOfTasks = taskList.childElementCount;
    return nOfTasks;
};
/**************************************************************************************************************************************************************************************/
/* function updateTaskCountView() --- calls functions to count tasks for each collumn and places those values in correct place */
/**************************************************************************************************************************************************************************************/
function updateTaskCountView(){
    const todoCount = countTODOTasks();
    const doingCount = countDOINGTasks();
    const doneCount = countDONETasks();
    const totalCount = todoCount + doingCount + doneCount;

    document.getElementById("todo-count").textContent = todoCount;
    document.getElementById("doing-count").textContent = doingCount;
    document.getElementById("done-count").textContent = doneCount;

    updateBarChart(todoCount, doingCount, doneCount, totalCount);
};
/**************************************************************************************************************************************************************************************/
/* function updateBarCharttodo, doing, done, total --- sets the top right element task-bar-char with correct proportions (visually a progress bar) */
/**************************************************************************************************************************************************************************************/
function updateBarChart(todo, doing, done, total) {
    const barChart = document.getElementById('task-bar-chart');
    barChart.innerHTML = ''; // Limpa o conteúdo anterior

    if (total > 0) {
        barChart.appendChild(createBarElement('todo', (todo / total) * 100));
        barChart.appendChild(createBarElement('doing', (doing / total) * 100));
        barChart.appendChild(createBarElement('done', (done / total) * 100));
    }
};
/**************************************************************************************************************************************************************************************/
/* function createBarElement(className, widthPercent) --- creates the progress bar subelement - each subpice used by function updateBarChart
/**************************************************************************************************************************************************************************************/
function createBarElement(className, widthPercent) {
    const bar = document.createElement('div');
    bar.classList.add('task-bar', className);
    bar.id = `${className}-bar`;
    bar.style.width = `${widthPercent}px`;
    bar.style.height = `${20}px`;
    return bar;
};
/**************************************************************************************************************************************************************************************/
/**************************************************************************************************************************************************************************************/