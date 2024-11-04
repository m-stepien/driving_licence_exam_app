//todo wyswietlanie wyniku testu
//todo stworzenie widoku rezultatu egzaminu

const server_url = "http://localhost:8080";
let numberOfQuestionByType;
let currentQuestionId;
let examSolution = {};
let basicNumber = 0;
let specializationNumber = 0;
let secondLeft = 20;
let timer;
let isReadingTime = true;
let questionMediaId;
let media;
let clickToStartImg;
async function initQuiz(){
await fetch(server_url + "/exam/" + category)
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log("Fetched Data:", data);
        numberOfQuestionByType = data;
    })
    .catch(error => {
        console.error('Fetch error:', error);
    });
    let question = await fetch(server_url + "/exam/question/current")
                       .then(response => {
                           if (!response.ok) {
                               throw new Error('Network response was not ok ' + response.statusText);
                           }
                           return response.json();
                       })
                       .then(data => {
                           console.log("Fetched Data:", data);
                           return data;
                       })
                       .catch(error => {
                           console.error('Fetch error:', error);
                       });
    if(question.media){
        media = question.media;
    }
    return question;
}

async function sendAnswers(){
    console.log("examSolution");
    console.log(examSolution);
    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    const response = await fetch(server_url + "/exam/send/solution", {
      method: "POST",
      headers: myHeaders,
      body: JSON.stringify(examSolution),
      })
    console.log(response.status);
    console.log(response);
}

function putQuestionInsideDOM(question){
    currentQuestionId = question.id;
    let questionElement = document.getElementById("question");
    questionElement.innerHTML = question.question;
    let optionsContainer = document.getElementById("options");
    putAnswersInsideOptionsContainer(question.answer, optionsContainer);
    let pointsElement = document.getElementById("points")
    pointsElement.innerHTML = question.points + " points";
}

function putAnswersInsideOptionsContainer(answers, container){
    if(answers==null){
        let container = document.getElementById("options");
        container.classList.remove("options-wrapped");
        let button = document.createElement('button');
        button.className = 'yn-button';
        button.textContent = 'Yes';
        button.dataset.value = 'y';
        button.onclick = function() {
            selectAnswer('y',this);

        };
        container.appendChild(button);
        let button2 = document.createElement('button');
        button2.className = 'yn-button';
        button2.dataset.value = 'n';
        button2.onclick = function() {
            selectAnswer('n', this);
        };
        button2.textContent = 'No';
        container.appendChild(button2);
    }
    else{
        let container = document.getElementById("options");
        container.classList.add("options-wrapped");
        const answerKeys = Object.keys(answers);
        answerKeys.forEach(key => {
                if(key!="id"){
                    let button = document.createElement('button');
                    button.className = 'abc-button';
                    button.dataset.value = key.slice(-1).toLowerCase();
                    button.textContent = answers[key];
                    button.onclick = function() {
                                    selectAnswer(this.getAttribute('data-value'), this);
                                };
                    container.appendChild(button);
                }

        }
        )
    }
}

async function nextQuestion(){
    await clearInterval(timer);
    createReadInterval();
    if(!examSolution[currentQuestionId]){
        examSolution[currentQuestionId] = null;
    }
    let question = await fetch(server_url + "/exam/question/next")
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
                    console.log("Fetched Data:", data);
                    return data;
                })
                .catch(error => {
                    console.error('Fetch error:', error);
                });
        if(question.media){
            media = question.media;
        }
        optionsConatiner = document.getElementById("options");
        optionsConatiner.replaceChildren();
        putQuestionInsideDOM(question);
        increaseQuestionNumber();
        putNumbersOfQuestionInDom();
        document.getElementById("media-container").appendChild(clickToStartImg);
        if(basicNumber+specializationNumber===numberOfQuestionByType.basic+numberOfQuestionByType.specialization){
            createFinishButton();
        }


}
function selectAnswer(answer, element){
    console.log("Select answer executed with");
    console.log(answer);
    examSolution[currentQuestionId] = answer;
    hightlightSelectedAnswer(element);
}

function putNumbersOfQuestionInDom(){
    basicElement = document.getElementById("basic-question-number");
    basicElement.innerHTML = basicNumber + "/" + numberOfQuestionByType.basic;
    specializationElement = document.getElementById("specialization-question-number");
    specializationElement.innerHTML = specializationNumber + "/" +numberOfQuestionByType.specialization;
    progressBar();
}

function increaseQuestionNumber(){
    if(basicNumber+specializationNumber<numberOfQuestionByType.basic){
        basicNumber+=1;
    }
    else{
        specializationNumber+=1;
    }
}

function loadImage(){
  document.getElementById("media-container").replaceChildren();
  if(media){
    let url = server_url + "/media/" + media.id;
    if(media.type==="jpg"){
        putImageInsideDom(url);
        createAnswerInterval();
    }
    else if(media.type==="wmv"){
        putVideoInsideDom(url);
    }
  }
  else{
    let url = server_url + "/media/0";
    putImageInsideDom(url);
    createAnswerInterval();
  }
}

async function createAnswerInterval(){
    secondLeft = 15;
    let infoTimerElement = document.getElementById("info-timer");
    infoTimerElement.innerHTML = "Time for answer";
    let timeElement = document.getElementById("time");
    timeElement.innerHTML = secondLeft+" s";
    timer = setInterval(function() {
                let timeElement = document.getElementById("time");
                secondLeft-=1;
                timeElement.innerHTML = secondLeft+" s";
                if(secondLeft == 0){
                    clearInterval(timer);
                    nextQuestion();
                }
            },1000);
}

function createFinishButton(){
    let finishButton = document.createElement('button');
    finishButton.className = 'option-btn';
    finishButton.textContent = 'Send';
    finishButton.onclick = function() {
                sendAnswers();
            };
    let nextButton = document.getElementById("next");
    nextButton.replaceWith(finishButton);
}

function createReadInterval(){
    isReadingTime=true;
    let mediaContainer = document.getElementById("media-container");
    mediaContainer.replaceChildren();
    secondLeft = 20;
    let infoTimerElement = document.getElementById("info-timer");
    infoTimerElement.innerHTML = "Time to read the question";
    timer = setInterval(function() {
        let timeElement = document.getElementById("time");
        timeElement.innerHTML = secondLeft+" s";
        if(secondLeft == 0){
            isReadingTime=false;
            clearInterval(timer);
            loadImage();
        }
        secondLeft-=1;
    },1000);
}

function progressBar(){
    let bPercent = basicNumber/numberOfQuestionByType.basic*100;
    let sPercent = specializationNumber/numberOfQuestionByType.specialization*100;
    document.getElementById("basic-bar").setAttribute("style","width:"+bPercent+"%");
    document.getElementById("special-bar").setAttribute("style","width:"+sPercent+"%");
}


function putVideoInsideDom(url){
     let mediaContainer = document.getElementById("media-container");
     let video = document.createElement("video");
     video.id = "videoPlayer";
     const source = document.createElement("source");
     source.src = url;
     source.type = "video/mp4";
     video.appendChild(source);
     mediaContainer.appendChild(video);
     media = null;
     video.muted = true;
     video.load();
     video.play();
     video.addEventListener("ended", () => {
        createAnswerInterval();
     })
     putWaitToVideoEndInfoInsideDOM();
}

async function putImageInsideDom(url){
    let imageBlob = await fetch(url, {
    method: 'GET',
    headers: {
        'Accept': 'image/jpeg',
    }
    }).then(res => res.blob());
    console.log(imageBlob);
    let imgElement = document.createElement("img");
    let urlImage = URL.createObjectURL(imageBlob);
    imgElement.src = urlImage;
    let imageContainer = document.getElementById("media-container");
    imageContainer.appendChild(imgElement);
    media = null;
}

function putWaitToVideoEndInfoInsideDOM(){
    let labelElement = document.getElementById("info-timer");
    labelElement.innerHTML = "video is playing";
    let timer = document.getElementById("time");
    timer.innerHTML = "...";
}

function skipReadingTime(){
    console.log("Inside skipReadingTime");
    if(isReadingTime){
        console.log("Inside if");
        isReadingTime=false;
        clearInterval(timer);
        loadImage();
    }
}

function hightlightSelectedAnswer(selectedButton){
    const selectedClassName = "button-selected";
    let buttons = document.getElementsByClassName("yn-button");
    if(buttons.length == 0){
        buttons = document.getElementsByClassName("abc-button");
    }
    for(let idx = 0; idx<buttons.length; idx++){
        buttons[idx].classList.remove(selectedClassName);
    }
    selectedButton.classList.add(selectedClassName);
}

let firstQuestion;
(async () =>{
clickToStartImg = document.getElementById("click-to-start");
firstQuestion = await initQuiz();
putQuestionInsideDOM(firstQuestion);
increaseQuestionNumber();
putNumbersOfQuestionInDom();
let timerInfo = document.getElementById("info-timer");
timerInfo.innerHTML = "Time to read the question";

timer = setInterval(function() {
    isReadingTime = true;
    let timeElement = document.getElementById("time");
    timeElement.innerHTML = secondLeft+" s";
    if(secondLeft == 0){
        isReadingTime=false;
        clearInterval(timer);
        loadImage();
    }
    secondLeft-=1;
},1000);
document.getElementById("media-container").addEventListener("click", skipReadingTime);
})();