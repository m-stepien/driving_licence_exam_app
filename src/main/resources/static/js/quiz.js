//todo showing image in website
//todo playing wmv in website
//todo wyswietlanie wyniku testu
//todo zapis wyników do bazy danych
const server_url = "http://localhost:8080";
let numberOfQuestionByType;
let currentQuestionId;
let examSolution = {};
let basicNumber = 0;
let specializationNumber = 0;
let secondLeft = 20;
let timer;
let questionMediaId;
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
    questionMediaId = question.media.id;
    getMedia()
    return question;
}

async function sendAnswers(){
    const response = await fetch(server_url + "/exam/send/solution", {
      method: "POST",
      body: JSON.stringify(examSolution),
      })
    console.log(response.status);
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
            selectAnswer('y');
        };
        container.appendChild(button);
        let button2 = document.createElement('button');
        button2.className = 'yn-button';
        button2.dataset.value = 'n';
        button2.onclick = function() {
            selectAnswer('n');
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
                                    selectAnswer(this.getAttribute('data-value'));
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
        questionMediaId = question.media.id;
        getMedia();
        optionsConatiner = document.getElementById("options");
        optionsConatiner.replaceChildren();
        putQuestionInsideDOM(question);
        increaseQuestionNumber();
        putNumbersOfQuestionInDom();
        if(basicNumber+specializationNumber===numberOfQuestionByType.basic+numberOfQuestionByType.specialization){
            createFinishButton();
        }


}
function selectAnswer(answer){
    console.log("Select answer executed with");
    console.log(answer);
    examSolution[currentQuestionId] = answer;
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
    console.log("Here code to put image or start video");
    //propably with something like setInterval to wait to the end of video
    createAnswerInterval();
}

async function createAnswerInterval(){
    secondLeft = 15;
    let infoTimerElement = document.getElementById("info-timer");
    infoTimerElement.innerHTML = "Time for answer";
    timer = setInterval(function() {
                let timeElement = document.getElementById("time");
                timeElement.innerHTML = secondLeft+" s";
                if(secondLeft == 0){
                    clearInterval(timer);
                    nextQuestion();
                }
                secondLeft-=1;
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
    secondLeft = 20;
    let infoTimerElement = document.getElementById("info-timer");
    infoTimerElement.innerHTML = "Time to read the question";
    timer = setInterval(function() {
        let timeElement = document.getElementById("time");
        timeElement.innerHTML = secondLeft+" s";
        if(secondLeft == 0){
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

async function getMedia(){
    if(questionMediaId){
    var url = server_url + "/media/" +questionMediaId;
        let media = await fetch(url, {
        method: 'GET',
        headers: {
        'Accept': 'image/jpeg',
        }
        }).then(res => res.blob());
        console.log(media)
        putMediaInsideDom(media);
        return media;
  }
  else{
    console.log("media id is null");
    return null;
  }
}

function putMediaInsideDom(mediaBlob){
    let imgElement = document.createElement("img");
    let url = URL.createObjectURL(mediaBlob);
    imgElement.src = url;
    imgElement.width = "500";
    let imageContainer = document.getElementById("media-container");
    imageContainer.replaceChildren();
    imageContainer.appendChild(imgElement);
}

let firstQuestion;
(async () =>{
firstQuestion = await initQuiz();
putQuestionInsideDOM(firstQuestion);
increaseQuestionNumber();
putNumbersOfQuestionInDom();
let timerInfo = document.getElementById("info-timer");
timerInfo.innerHTML = "Time to read the question";

timer = setInterval(function() {
    let timeElement = document.getElementById("time");
    timeElement.innerHTML = secondLeft+" s";
    if(secondLeft == 0){
        clearInterval(timer);
        loadImage();
    }
    secondLeft-=1;
},1000);
})();