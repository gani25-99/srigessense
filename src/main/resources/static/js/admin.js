const form = document.getElementById("productForm");

// =======================
// LOAD CATEGORIES
// =======================

async function loadCategories() {

    try {

        const response = await fetch("/api/category");

        const categories = await response.json();

        const categorySelect = document.getElementById("categoryId");

        categorySelect.innerHTML =
            '<option value="">Select Category</option>';

        categories.forEach(category => {

            categorySelect.innerHTML += `
                <option value="${category.id}">
                    ${category.name}
                </option>
            `;

        });

    }

    catch (e) {

        console.log(e);

        alert("Unable to load categories");

    }

}

// =======================
// LOAD SUB CATEGORIES
// =======================

// =======================
// LOAD SUB CATEGORIES
// =======================

async function loadSubCategories(categoryId) {

    console.log("Selected Category ID:", categoryId);

    const subSelect = document.getElementById("subcategoryId");

    subSelect.innerHTML =
        '<option value="">Select Sub Category</option>';

    if (!categoryId) {
        return;
    }

    try {

        const response = await fetch("/api/subcategory/category/" + categoryId);

        console.log("Response Status:", response.status);

        if (!response.ok) {
            throw new Error("API Error");
        }

        const subCategories = await response.json();

        console.log("Sub Categories:", subCategories);

        subCategories.forEach(sub => {

            subSelect.innerHTML += `
                <option value="${sub.id}">
                    ${sub.name}
                </option>
            `;

        });

    }

    catch (e) {

        console.error(e);

        alert("Unable to load sub categories");

    }

}

// =======================
// DISPLAY PRODUCTS
// =======================

function displayProducts(products) {

    let html = "";

    products.forEach(product => {

        html += `

        <div class="col-md-4 mb-4">

            <div class="card shadow h-100">

                <img
                    src="/products/${product.image}"
                    class="card-img-top"
                    style="height:220px;object-fit:cover;"
                    onerror="this.src='/images/logo3.png'">

                <div class="card-body">

                    <h5>${product.name}</h5>

                    <p>${product.description}</p>

                    <h6 class="text-success">

                        ₹ ${product.price}

                    </h6>

                    <p>

                        <strong>Category :</strong>

                        ${product.category.name}

                    </p>

                    <p>

                        <strong>Sub Category :</strong>

                        ${product.subCategory
                            ? product.subCategory.name
                            : "-"}

                    </p>

                    <p>

                        <strong>Stock :</strong>

                        ${product.quantity}

                    </p>

                    <div class="mt-3">

                        <button
                            class="btn btn-warning btn-sm me-2"
                            onclick="editProduct(${product.id})">

                            ✏ Edit

                        </button>

                        <button
                            class="btn btn-danger btn-sm"
                            onclick="deleteProduct(${product.id})">

                            🗑 Delete

                        </button>

                    </div>

                </div>

            </div>

        </div>

        `;

    });

    document.getElementById("products").innerHTML = html;

}
// =======================
// LOAD PRODUCTS
// =======================

async function loadProducts() {

    try {

        const response = await fetch("/api/product");

        const products = await response.json();

        displayProducts(products);

    }

    catch (e) {

        console.log(e);

    }

}

// =======================
// SAVE / UPDATE PRODUCT
// =======================

form.addEventListener("submit", async function (e) {

    e.preventDefault();

    const id = document.getElementById("productId").value;

    // =======================
    // SAVE
    // =======================

    if (id === "") {

        const formData = new FormData(form);

        try {

            const response = await fetch("/api/product/upload", {

                method: "POST",

                body: formData

            });

            const message = await response.text();

            if (!response.ok) {

                alert(message);

                return;

            }

            alert("Product Saved Successfully");

        }

        catch (e) {

            console.log(e);

            alert("Save Failed");

            return;

        }

    }

    // =======================
    // UPDATE
    // =======================

    else {

        const product = {

            name: form.name.value,

            description: form.description.value,

            price: Number(form.price.value),

            quantity: Number(form.quantity.value),

            category: {

                id: Number(form.categoryId.value)

            },

            subCategory: {

                id: Number(form.subcategoryId.value)

            }

        };

        try {

            const response = await fetch("/api/product/" + id, {

                method: "PUT",

                headers: {

                    "Content-Type": "application/json"

                },

                body: JSON.stringify(product)

            });

            const message = await response.text();

            if (!response.ok) {

                alert(message);

                return;

            }

            alert("Product Updated Successfully");

        }

        catch (e) {

            console.log(e);

            alert("Update Failed");

            return;

        }

    }

    form.reset();

    document.getElementById("productId").value = "";

    document.getElementById("subcategoryId").innerHTML =
        '<option value="">Select Sub Category</option>';

    loadProducts();

});

// =======================
// DELETE PRODUCT
// =======================

async function deleteProduct(id) {

    if (!confirm("Delete this product?")) {

        return;

    }

    try {

        const response = await fetch("/api/product/" + id, {

            method: "DELETE"

        });

        const message = await response.text();

        if (!response.ok) {

            alert(message);

            return;

        }

        alert("Product Deleted Successfully");

        loadProducts();

    }

    catch (e) {

        console.log(e);

        alert("Delete Failed");

    }

}

// =======================
// EDIT PRODUCT
// =======================

async function editProduct(id) {

    const response = await fetch("/api/product/" + id);

    const product = await response.json();

    document.getElementById("productId").value = product.id;

    form.name.value = product.name;

    form.description.value = product.description;

    form.price.value = product.price;

    form.quantity.value = product.quantity;

    form.categoryId.value = product.category.id;

    await loadSubCategories(product.category.id);

    if (product.subCategory != null) {

        form.subcategoryId.value = product.subCategory.id;

    }

    window.scrollTo({

        top: 0,

        behavior: "smooth"

    });

}
// =======================
// SEARCH PRODUCTS
// =======================

async function searchProducts() {

    const keyword =
        document.getElementById("search").value.trim();

    if (keyword === "") {

        loadProducts();

        return;

    }

    try {

        const response =
            await fetch("/api/product/search?keyword=" + encodeURIComponent(keyword));

        const products =
            await response.json();

        displayProducts(products);

    }

    catch (e) {

        console.log(e);

    }

}

// =======================
// EVENTS
// =======================

document.getElementById("search")
        .addEventListener("keyup", searchProducts);

document.getElementById("categoryId")
        .addEventListener("change", function () {

            loadSubCategories(this.value);

        });

// =======================
// INITIAL LOAD
// =======================

window.onload = async function () {

    await loadCategories();

    await loadProducts();

};