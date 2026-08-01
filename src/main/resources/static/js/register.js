// ==========================
// Password Show / Hide
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
            strengthBar.style.background = "red";
            strengthText.innerHTML = "Weak Password";
            break;

        case 2:
            strengthBar.style.width = "50%";
            strengthBar.style.background = "orange";
            strengthText.innerHTML = "Medium Password";
            break;

        case 3:
            strengthBar.style.width = "75%";
            strengthBar.style.background = "#0d6efd";
            strengthText.innerHTML = "Good Password";
            break;

        case 4:
            strengthBar.style.width = "100%";
            strengthBar.style.background = "green";
            strengthText.innerHTML = "Strong Password";
            break;
    }

});

// ==========================
// Confirm Password Validation
// ==========================

document.querySelector("form").addEventListener("submit", function (e) {

    const pwd = document.getElementById("password").value;

    const confirmPwd = document.getElementById("confirmPassword").value;

    if (pwd !== confirmPwd) {

        e.preventDefault();

        alert("Password and Confirm Password do not match.");

        return;

    }

    if (pwd.length < 8) {

        e.preventDefault();

        alert("Password must contain at least 8 characters.");

    }

});