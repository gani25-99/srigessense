let count = 30;

function sendOtp(){

let mobile=document.getElementById("mobile").value;

if(mobile.length!=10){

alert("Enter Valid Mobile Number");

return;

}

fetch("/api/auth/send-otp",{

method:"POST",

headers:{

"Content-Type":"application/json"

},

body:JSON.stringify({

mobile:mobile

})

})

.then(res=>res.text())

.then(data=>{

alert(data);

document.getElementById("otpSection").style.display="block";

startTimer();

});

}

function verifyOtp(){

let mobile=document.getElementById("mobile").value;

let otp=document.getElementById("otp").value;

fetch("/api/auth/verify-otp",{

method:"POST",

headers:{

"Content-Type":"application/json"

},

body:JSON.stringify({

mobile:mobile,

otp:otp

})

})

.then(res=>res.text())

.then(data=>{

alert(data);

if(data==="Login Successful" ||

data==="New User Registered Successfully"){

window.location="/home";

}

});

}

function startTimer(){

count=20;

let timer=setInterval(function(){

count--;

document.getElementById("timer").innerHTML=

"Resend OTP in "+count+" sec";

if(count==0){

clearInterval(timer);

document.getElementById("timer").innerHTML=

"<a href='#' onclick='sendOtp()'>Resend OTP</a>";

}

},1000);

}