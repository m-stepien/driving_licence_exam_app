//todo fetch score from backend
//todo create endpoint to fetch score with user authorization
//todo add exam category to score entity
//todo save question answer in db
//todo fetch question answer from backend
//todo create view that allow to see your answer and correct answer of question

const server_url = "http://localhost:8080";
let score;
async function fetchScore(){
    let id = window.location.href.split("/").slice(-1);
    score = await fetch(server_url + "exam/score/" + id)
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    console.log(score);
}


(async()=>{


})();