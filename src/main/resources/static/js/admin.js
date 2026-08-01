const form = document.getElementById("productForm");

// =======================
// Load Categories
// =======================

async function loadCategories() {

    const response = await fetch("/api/category");
    const categories = await response.json();

    let options = '<option value="">Select Category</option>';

    categories.forEach(category => {

        options += `
            <option value="${category.id}">
                ${category.name}
            </option>
        `;

    });

    document.getElementById("categoryId").innerHTML = options;

}

// =======================
// Display Products
// =======================

function displayProducts(products) {

    let html = "";

    products.forEach(product => {

        html += `

        <div class="col-md-4 mb-4">

            <div class="card shadow h-100">

                <img
                    src="/images/products/${product.image}"
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
                        <strong>Category:</strong>
                        ${product.category.name}
                    </p>

                    <p>
                        <strong>Stock:</strong>
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
// Load Products
// =======================

async function loadProducts() {

    const response = await fetch("/api/product");
    const products = await response.json();

    displayProducts(products);

}

// =======================
// Save / Update Product
// =======================

form.addEventListener("submit", async function (e) {

    e.preventDefault();

    const id = document.getElementById("productId").value;

    if (id === "") {

        const data = new FormData(form);

        const response = await fetch("/api/product/upload", {

            method: "POST",
            body: data

        });

        const message = await response.text();

        if (!response.ok) {

            alert("Save Failed\n\n" + message);
            return;

        }

        alert("Product Saved Successfully");

    } else {

        const product = {

            name: form.name.value,
            description: form.description.value,
            price: Number(form.price.value),
            quantity: Number(form.quantity.value),

            category: {

                id: Number(form.categoryId.value)

            }

        };

        const response = await fetch("/api/product/" + id, {

            method: "PUT",

            headers: {

                "Content-Type": "application/json"

            },

            body: JSON.stringify(product)

        });

        const message = await response.text();

        if (!response.ok) {

            alert("Update Failed\n\n" + message);
            return;

        }

        alert("Product Updated Successfully");

    }

    form.reset();

    document.getElementById("productId").value = "";

    loadProducts();

});

// =======================
// Delete Product
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

        if (response.ok) {

            alert(message);

            loadProducts();

        } else {

            alert("Delete Failed\n\n" + message);

        }

    } catch (e) {

        alert("Server Error");

        console.log(e);

    }

}

// =======================
// Edit Product
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

    window.scrollTo({

        top: 0,

        behavior: "smooth"

    });

}

// =======================
// Search Products
// =======================

async function searchProducts() {

    const keyword = document.getElementById("search").value;

    if (keyword.trim() === "") {

        loadProducts();
        return;

    }

    const response = await fetch("/api/product/search?keyword=" + keyword);

    const products = await response.json();

    displayProducts(products);

}

// =======================
// Initial Load
// =======================

document.getElementById("search")
        .addEventListener("keyup", searchProducts);

loadCategories();
loadProducts();