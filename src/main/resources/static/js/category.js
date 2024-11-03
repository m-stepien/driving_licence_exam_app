const server  = "http://localhost:8080";
let categories = [];
async function fetchAllCategoryName(){
    let response = await fetch(server + "/exam/category/all");
    if(!response.ok){
        Console.err("Error during fetchin data drom " + "/exam/category/all")
    }
    else{
        categories = await response.json();
    }
    return 0;
}

function putCategoriesInsideNavbar(){
    let menuElement = document.getElementById("menu");
    const linkPrefix = "/test/";
    for(let idx=0; idx<categories.length; idx++){
            console.log(categories[idx])
            let liElem = document.createElement("li");
            let linkElem = document.createElement("a");
            linkElem.innerHTML = categories[idx].name;
            linkElem.classList.add("nav-link");
            linkElem.href = server + linkPrefix + categories[idx].name;
            liElem.classList.add("nav-item");
            liElem.appendChild(linkElem);
            menuElement.insertBefore(liElem, menuElement.firstChild);
    }
}

(async ()=>{
    await fetchAllCategoryName();
    putCategoriesInsideNavbar();
})();