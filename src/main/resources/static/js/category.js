const server_url  = "http://localhost:8080";
let category = [];
async function fetchAllCategoryName(){
    let response = await fetch(server_url + "/exam/category/all");
    if(!response.ok){
        Console.err("Error during fetchin data drom " + "/exam/category/all")
    }
    else{
        category = await response.json();
        console.log("INSIDE" + category.size);
    }
    return 0;
}

function putCategoriesInsideNavbar(){
    console.log("puttingi inside");
    let menuElement = document.getElementById("menu");
    console.log(category.size)
    for(let idx=0; idx<category.size; idx++){
            console.log("estem")
            let liElem = document.createElement("li");
            liElem.innerHTML = cat.name;
            menuElement.appendChild(menuElement);
    }
//    category.foreach((cat) => {
//        let liElem = document.createElement("li");
//        liElem.innerHTML = cat.name;
//        menuElement.appendChild(menuElement);
//    });
}

(async ()=>{
    await fetchAllCategoryName();
    putCategoriesInsideNavbar();
})();