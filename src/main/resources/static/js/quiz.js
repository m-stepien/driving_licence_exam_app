const server_url = "http://localhost:8080";
//exam/{category}
//order
//get category from url
//init exam
function initQuiz(){
let question = fetch(server_url + "/exam/" + category)
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
    return question;
}

function putQuestionInsideDOM(question){
}

//function fetchCurrentQuestion(){
////get first question to global variable
//}
//
//()=>{
//    initQuiz();
//}
let firstQuestion = initQuiz();
