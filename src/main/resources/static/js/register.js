// ==========================
// Show / Hide Password
// ==========================

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

// ==========================
// Password Strength
// ==========================

const password = document.getElementById("password");
const strengthBar = document.getElementById("strengthBar");
const strengthText = document.getElementById("strengthText");

if (password) {

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

// ==========================
// Mobile Validation
// ==========================

const mobile = document.querySelector("input[name='mobile']");

if (mobile) {

    mobile.addEventListener("input", function () {

        this.value = this.value.replace(/\D/g, "");

    });

}

// ==========================
// Form Validation
// ==========================

const form = document.querySelector("form");

if (form) {

    form.addEventListener("submit", function (e) {

        const email =
            document.querySelector("input[name='email']").value.trim();

        const mobile =
            document.querySelector("input[name='mobile']").value.trim();

        const pwd =
            document.getElementById("password").value;

        const confirmPwd =
            document.getElementById("confirmPassword").value;

        const emailPattern =
            /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailPattern.test(email)) {

            e.preventDefault();

            alert("Enter a valid email address.");

            return;

        }

        if (mobile.length !== 10) {

            e.preventDefault();

            alert("Mobile number must contain exactly 10 digits.");

            return;

        }

        if (pwd.length < 8) {

            e.preventDefault();

            alert("Password must contain at least 8 characters.");

            return;

        }

        if (pwd !== confirmPwd) {

            e.preventDefault();

            alert("Password and Confirm Password do not match.");

            return;

        }

        const button = form.querySelector("button");

        button.disabled = true;

        button.innerHTML =
            "<span class='spinner-border spinner-border-sm'></span> Creating Account...";

    });

}
