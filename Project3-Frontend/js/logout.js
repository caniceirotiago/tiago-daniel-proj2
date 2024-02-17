
export function clickOnLogout(){
    const logoutbtn = document.getElementById("nav-exit");
    logoutbtn.addEventListener('click',function(event){
        const username = localStorage.getItem('username');

        logout(username);
    })  
}

function logout(username){
    console.log(username);
    alert('Sucessfull Logout');
    console.log("Logout");
    localStorage.removeItem('username'); 
    localStorage.removeItem('password');
    localStorage.removeItem("photoUrl"); 
    window.location.href = "index.html";  
}
