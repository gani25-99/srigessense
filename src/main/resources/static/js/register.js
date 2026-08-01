// ===============================
// Show / Hide Password
// ===============================

function togglePassword(id, icon){

    const input = document.getElementById(id);

    if(input.type === "password"){

        input.type = "text";

        icon.classList.remove("fa-eye");

        icon.classList.add("fa-eye-slash");

    }else{

        input.type = "password";

        icon.classList.remove("fa-eye-slash");

        icon.classList.add("fa-eye");

    }

}

// ===============================
// Password Strength
// ===============================

const password = document.getElementById("password");

if(password){

password.addEventListener("keyup",function(){

    let value=password.value;

    let strength=0;

    if(value.length>=8) strength++;

    if(/[A-Z]/.test(value)) strength++;

    if(/[a-z]/.test(value)) strength++;

    if(/[0-9]/.test(value)) strength++;

    if(/[^A-Za-z0-9]/.test(value)) strength++;

    const bar=document.getElementById("strengthBar");

    const text=document.getElementById("strengthText");

    switch(strength){

        case 1:

            bar.style.width="20%";
            bar.style.background="#f44336";
            text.innerHTML="Weak";
            break;

        case 2:

            bar.style.width="40%";
            bar.style.background="#ff9800";
            text.innerHTML="Fair";
            break;

        case 3:

            bar.style.width="60%";
            bar.style.background="#ffc107";
            text.innerHTML="Good";
            break;

        case 4:

            bar.style.width="80%";
            bar.style.background="#4caf50";
            text.innerHTML="Strong";
            break;

        case 5:

            bar.style.width="100%";
            bar.style.background="#2e7d32";
            text.innerHTML="Very Strong";
            break;

        default:

            bar.style.width="0";
            text.innerHTML="Password Strength";

    }

});

}

// ===============================
// Confirm Password Validation
// ===============================

const form=document.querySelector("form");

if(form){

form.addEventListener("submit",function(e){

    let pass=document.getElementById("password").value;

    let confirm=document.getElementById("confirmPassword").value;

    if(pass!==confirm){

        e.preventDefault();

        showToast("Passwords do not match","error");

        return;

    }

    showLoader();

});

}

// ===============================
// Mobile Validation
// ===============================

const mobile=document.querySelector("input[name='mobile']");

if(mobile){

mobile.addEventListener("input",function(){

    this.value=this.value.replace(/\D/g,'');

    if(this.value.length>10){

        this.value=this.value.slice(0,10);

    }

});

}

// ===============================
// Email Validation
// ===============================

const email=document.querySelector("input[name='email']");

if(email){

email.addEventListener("blur",function(){

    if(this.value==="") return;

    let regex=/^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if(!regex.test(this.value)){

        showToast("Invalid Email Address","error");

    }

});

}

// ===============================
// Loader
// ===============================

function showLoader(){

    let loader=document.querySelector(".loader");

    if(loader){

        loader.style.display="block";

    }

}

function hideLoader(){

    let loader=document.querySelector(".loader");

    if(loader){

        loader.style.display="none";

    }

}

// ===============================
// Toast
// ===============================

function showToast(message,type){

    let toast=document.querySelector(".toast");

    if(!toast){

        toast=document.createElement("div");

        toast.className="toast";

        document.body.appendChild(toast);

    }

    toast.innerHTML=message;

    toast.className="toast show "+type;

    setTimeout(function(){

        toast.className="toast";

    },3000);

}