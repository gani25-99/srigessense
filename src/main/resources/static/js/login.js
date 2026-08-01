const mobileTab = document.getElementById("mobileTab");
const emailTab = document.getElementById("emailTab");

const mobileLogin = document.getElementById("mobileLogin");
const emailLogin = document.getElementById("emailLogin");

mobileTab.addEventListener("click", function () {

    mobileTab.classList.add("active");
    emailTab.classList.remove("active");

    mobileLogin.style.display = "block";
    emailLogin.style.display = "none";

});

emailTab.addEventListener("click", function () {

    emailTab.classList.add("active");
    mobileTab.classList.remove("active");

    emailLogin.style.display = "block";
    mobileLogin.style.display = "none";

});