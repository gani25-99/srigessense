// ==============================
// Elements
// ==============================

const emailTab = document.getElementById("emailTab");
const mobileTab = document.getElementById("mobileTab");

const emailLogin = document.getElementById("emailLogin");
const mobileLogin = document.getElementById("mobileLogin");

// ==============================
// Default
// ==============================

emailLogin.style.display = "block";
mobileLogin.style.display = "none";

// ==============================
// Email Login Tab
// ==============================

emailTab.addEventListener("click", function () {

    emailLogin.style.display = "block";
    mobileLogin.style.display = "none";

    emailTab.classList.add("active");
    mobileTab.classList.remove("active");

});

// ==============================
// Mobile Login Tab
// ==============================

mobileTab.addEventListener("click", function () {

    emailLogin.style.display = "none";
    mobileLogin.style.display = "block";

    mobileTab.classList.add("active");
    emailTab.classList.remove("active");

});

// ==============================
// Show / Hide Password
// ==============================

function togglePassword() {

    const password = document.getElementById("password");

    const eye = document.getElementById("eye");

    if (password.type === "password") {

        password.type = "text";

        eye.classList.remove("bi-eye");
        eye.classList.add("bi-eye-slash");

    } else {

        password.type = "password";

        eye.classList.remove("bi-eye-slash");
        eye.classList.add("bi-eye");

    }

}

// ==============================
// Mobile Validation
// ==============================

const mobileInput = document.querySelector(
    "#mobileLogin input[name='mobile']"
);

if (mobileInput) {

    mobileInput.addEventListener("input", function () {

        this.value = this.value.replace(/\D/g, "");

    });

}

// ==============================
// Email Validation
// ==============================

const emailForm = document.querySelector("#emailLogin form");

if (emailForm) {

    emailForm.addEventListener("submit", function (e) {

        const email =
            this.querySelector("input[name='email']").value.trim();

        const password =
            this.querySelector("input[name='password']").value;

        const emailPattern =
            /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailPattern.test(email)) {

            e.preventDefault();

            alert("Please enter a valid email address.");

            return;

        }

        if (password.length < 6) {

            e.preventDefault();

            alert("Password must contain at least 6 characters.");

        }

    });

}

// ==============================
// Mobile Form Validation
// ==============================

const mobileForm = document.querySelector("#mobileLogin form");

if (mobileForm) {

    mobileForm.addEventListener("submit", function (e) {

        const mobile =
            this.querySelector("input[name='mobile']").value;

        if (mobile.length !== 10) {

            e.preventDefault();

            alert("Please enter a valid 10-digit mobile number.");

        }

    });

}

// ==============================
// Button Loading Effect
// ==============================

document.querySelectorAll("form").forEach(function (form) {

    form.addEventListener("submit", function () {

        const button = this.querySelector("button");

        if (button) {

            button.disabled = true;

            button.innerHTML =
                "<span class='spinner-border spinner-border-sm'></span> Please Wait...";

        }

    });

});
