
const logoutbtn = document.getElementById(nav-exit);

export function clickOnLogout(){
    logoutbtn.addEventListener('click',function(event){
    const username = localStorage.getItem('username');
    logout(username);
})
}

/* Pedido de logout ao backend */ 
async function logout(username){
    console.log(username);
    await fetch('http://localhost:8080/Project3-Backend/rest/user/logout',
        {
            method: 'POST',
            headers:
        {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'username' : username
        },
            credentials: 'include',
        }
        ).then(function (response) {
            if (response.status == 200) { 
                alert('Sucessfull Logout');   
                window.location.href = "homepage.html";  
                localStorage.removeItem('username') ;
            } else {
                alert('something went wrong :(');
         }
    });
}
