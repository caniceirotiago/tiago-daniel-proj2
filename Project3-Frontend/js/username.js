/**************************************************************************************************************************************************************************************/ 
/* SET USERNAME INTO HEADER  */
/**************************************************************************************************************************************************************************************/ 
export function setUsername(){
        let storedUsername = localStorage.getItem('username');
        if (storedUsername) {
            document.getElementById('usernameDisplay').textContent = storedUsername;
        }
        else { // default (should never ever happen)
            document.getElementById('usernameDisplay').textContent = "Username";
        }
};


/*
Requires: on .html -> both need to be type="module"
<script defer src="homepage.js" type="module"></script>
<script defer src="username.js" type="module"></script>

Requires on *main*.js
import { setUsername } from "./username.js";
...
setUsername();


*/