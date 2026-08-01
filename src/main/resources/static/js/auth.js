// ===============================
// TAB SWITCH (Optional)
// ===============================

const emailForm = document.getElementById("emailForm");
const mobileForm = document.getElementById("mobileForm");

// ===============================
// SHOW / HIDE PASSWORD
// ===============================

function togglePassword(id, icon) {

    const input = document.getElementById(id);

    if (input.type === "password") {

        input.type = "text";

        icon.classList.remove("fa-eye");

        icon.classList.add("fa-eye-slash");

    } else {

        input.type = "password";

        icon.classList.remove("fa-eye-slash");

        icon.classList.add("fa-eye");

    }

}

// ===============================
// MOBILE NUMBER VALIDATION
// ===============================

document.querySelectorAll("input[name='mobile']").forEach(input => {

    input.addEventListener("input", function () {

        this.value = this.value.replace(/\D/g, "");

        if (this.value.length > 10) {

            this.value = this.value.substring(0, 10);

        }

    });

});

// ===============================
// EMAIL LOGIN VALIDATION
// ===============================

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

            alert("Enter a valid Email Address.");

            return;

        }

        if (password.length < 8) {

            e.preventDefault();

            alert("Password must contain at least 8 characters.");

            return;

        }

        const button = this.querySelector("button");

        button.disabled = true;

        button.innerHTML =
            "<span class='spinner-border spinner-border-sm'></span> Please Wait...";

    });

}

// ===============================
// MOBILE LOGIN VALIDATION
// ===============================

if (mobileForm) {

    mobileForm.addEventListener("submit", function (e) {

        const mobile =
            this.querySelector("input[name='mobile']").value;

        if (mobile.length !== 10) {

            e.preventDefault();

            alert("Enter a valid 10 digit Mobile Number.");

            return;

        }

        const button = this.querySelector("button");

        button.disabled = true;

        button.innerHTML =
            "<span class='spinner-border spinner-border-sm'></span> Sending OTP...";

    });

}

// ===============================
// PASSWORD STRENGTH
// ===============================

const password =
    document.getElementById("password");

const strengthBar =
    document.getElementById("strengthBar");

const strengthText =
    document.getElementById("strengthText");

if (password && strengthBar && strengthText) {

    password.addEventListener("input", function () {

        let value = password.value;

        let strength = 0;

        if (value.length >= 8) strength++;

        if (/[A-Z]/.test(value)) strength++;

        if (/[0-9]/.test(value)) strength++;

        if (/[^A-Za-z0-9]/.test(value)) strength++;

        switch (strength) {

            case 0:
            case 1:

                strengthBar.style.width = "25%";
                strengthBar.style.background = "#dc3545";
                strengthText.innerHTML = "Weak Password";

                break;

            case 2:

                strengthBar.style.width = "50%";
                strengthBar.style.background = "#fd7e14";
                strengthText.innerHTML = "Medium Password";

                break;

            case 3:

                strengthBar.style.width = "75%";
                strengthBar.style.background = "#0d6efd";
                strengthText.innerHTML = "Good Password";

                break;

            case 4:

                strengthBar.style.width = "100%";
                strengthBar.style.background = "#198754";
                strengthText.innerHTML = "Strong Password";

                break;

        }

    });

}

// ===============================
// REGISTER VALIDATION
// ===============================

const registerForm =
    document.getElementById("registerForm");

if (registerForm) {

    registerForm.addEventListener("submit", function (e) {

        const pwd =
            document.getElementById("password").value;

        const confirm =
            document.getElementById("confirmPassword").value;

        if (pwd !== confirm) {

            e.preventDefault();

            alert("Passwords do not match.");

            return;

        }

    });

}