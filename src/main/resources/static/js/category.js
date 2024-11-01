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
    let menuElement = document.getElementById("menu");
    const linkPrefix = "test/";
    for(let idx=0; idx<category.length; idx++){
            console.log(category[idx])
            let liElem = document.createElement("li");
            let linkElem = document.createElement("a");
            linkElem.innerHTML = category[idx].name;
            linkElem.classList.add("nav-link");
            linkElem.href = linkPrefix + category[idx].name;
            liElem.classList.add("nav-item");
            liElem.appendChild(linkElem);
            menuElement.insertBefore(liElem, menuElement.firstChild);
    }
}

(async ()=>{
    await fetchAllCategoryName();
    putCategoriesInsideNavbar();
})();