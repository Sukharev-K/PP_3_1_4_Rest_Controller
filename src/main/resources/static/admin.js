let tableUsers = [];
let currentUser = "";
let deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
let editModal = new bootstrap.Modal(document.getElementById('editModal'));
let request = new Request("http://localhost:8080/api/admin", {
    method: "GET",
    headers: {
        'Content-Type': 'application/json',
    },
});
getUsers();

function getUsers() {
    fetch(request).then(res => res.json())
        .then(data => {
            tableUsers = [];
            if (data.length > 0) {
                data.forEach(user => {
                    tableUsers.push(user)
                })
            } else {
                tableUsers = [];
            }

            showUsers(tableUsers);
        })
}

fetch("http://localhost:8080/api/admin/edit").then(res => res.json())
    .then(data => {
        currentUser = data;
        console.log(data);
        showOneUser(currentUser);
    })

function showUsers(table) {
    let temp = "";
    table.forEach(user => {
        temp += "<tr>"
        temp += "<td>" + user.id + "</td>>"
        temp += "<td>" + user.firstName + "</td>>"
        temp += "<td>" + user.lastName + "</td>>"
        temp += "<td>" + user.passportNumberSeries + "</td>>"
        temp += "<td>" + user.login + "</td>>"
        temp += "<td>" + user.roles + "</td>>"
        temp += "<td>" + `<a onClick='showEditModal(${user.id})' class="btn btn-outline-info" id="edit">Edit User</a>` + "</td>>"
        temp += "<td>" + `<a onclick='showDeleteModal(${user.id})' class="btn btn-outline-danger" id="delete">Delete User</a>` + "</td>>"
        temp += "</tr>"
        document.getElementById("allUsersBody").innerHTML = temp;
    })
}

function getRoles(list) {
    let userRoles = [];
    for (let role of list) {
        if (role == 1 || role.id == 1) {
            userRoles.push("ADMIN");
        }
        if (role == 2 || role.id == 2) {
            userRoles.push("USER")
        }
    }
    return userRoles.join(" , ");
}

function showOneUser(user) {
    let temp = "";
    temp += "<tr>"
    temp += "<td>" + user.id + "</td>>"
    temp += "<td>" + user.firstName + "</td>>"
    temp += "<td>" + user.lastName + "</td>>"
    temp += "<td>" + user.passportNumberSeries + "</td>>"
    temp += "<td>" + user.login + "</td>>"
    temp += "<td>" + user.roles + "</td>>"
    temp += "</tr>"
    document.getElementById("oneUserBody").innerHTML = temp;
}

function rolesUser(event) {
    let rolesAdmin = {};
    let rolesUser = {};
    let roles = [];
    let allRoles = [];
    let sel = document.querySelector(event);
    for (let i = 0, n = sel.options.length; i < n; i++) {
        if (sel.options[i].selected) {
            roles.push(sel.options[i].value);
        }
    }
    if (roles.includes('1')) {
        rolesAdmin["id"] = 1;
        rolesAdmin["nameRole"] = "ROLE_ADMIN";
        allRoles.push(rolesAdmin);
    }
    if (roles.includes('2')) {
        rolesUser["id"] = 2;
        rolesUser["nameRole"] = "ROLE_USER";
        allRoles.push(rolesUser);
    }
    return allRoles;
}

document.getElementById('newUser').addEventListener('submit', addNewUser);

function addNewUser(form) {
    form.preventDefault();
    let newUserForm = new FormData(form.target);
    let user = {
        id: null,
        firstName: newUserForm.get('firstName'),
        lastName: newUserForm.get('lastName'),
        passportNumberSeries: newUserForm.get('passportNumberSeries'),
        login: newUserForm.get('login'),
        password: newUserForm.get('password'),
        roles: rolesUser("#roles")
    };

    let req = new Request("http://localhost:8080/api/admin",{
        method: 'POST',
        body: JSON.stringify(user),
        headers: {
            'Content-Type': 'application/json'
        }
    })

    fetch(req).then(() => getUsers())
    form.target.reset();

    const triggerE1 = document.querySelector('#v-pills-tabContent button[data-bs-target="#nav-home"]');
    bootstrap.Tab.getInstance(triggerE1).show();
}

function showDeleteModal(id) {
    document.getElementById('closeDeleteModal').setAttribute('onclick', () => {
        deleteModal.hide();
        document.getElementById('deleteUser').reset();
    });

    let request = new Request("http://localhost:8080/api/admin/" + id, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    fetch(request).then(res => res.json()).then(deleteUser => {
        console.log(deleteUser);
        document.getElementById('idDelete').setAttribute('value', deleteUser.id);
        document.getElementById('firstNameDelete').setAttribute('value', deleteUser.firstName);
        document.getElementById('lastNameDelete').setAttribute('value', deleteUser.lastName);
        document.getElementById('passportNumberSeriesDelete').setAttribute('value', deleteUser.passportNumberSeries);
        document.getElementById('loginDelete').setAttribute('value', deleteUser.login);
        document.getElementById('passwordDelete').setAttribute('value', deleteUser.password);
        if (getRoles(deleteUser.roles).includes("USER") && getRoles(deleteUser.roles).includes("ADMIN")) {
            document.getElementById('rolesDelete1').setAttribute('selected', 'true');
            document.getElementById('rolesDelete2').setAttribute('selected', 'true');
        } else if (getRoles(deleteUser.roles).includes("ADMIN")) {
            document.getElementById('rolesDelete1').setAttribute('selected', 'true');
        } else if (getRoles(deleteUser.roles).includes("USER")) {
            document.getElementById('rolesDelete2').setAttribute('selected', 'true');
        }
        deleteModal.show();

    }
    );
    var isDelete = false;
    document.getElementById('deleteUser').addEventListener('submit', event => {
        event.preventDefault();
        if (!isDelete) {
            isDelete = true;
            let request = new Request('http://localhost:8080/api/admin/' + id, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            fetch(request).then(() => {
                getUsers();
            });
            document.getElementById('deleteUser').reset();
        }

        deleteModal.hide();
    });
}

function showEditModal(id) {
    let request = new Request("http://localhost:8080/api/admin/" + id, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });
    fetch(request).then(res => res.json()).then(editUser => {
        document.getElementById('idEdit').setAttribute('value', editUser.id);
        document.getElementById('firstNameEdit').setAttribute('value', editUser.firstName);
        document.getElementById('lastNameEdit').setAttribute('value', editUser.lastName);
        document.getElementById('passportNumberSeriesEdit').setAttribute('value', editUser.passportNumberSeries);
        document.getElementById('loginEdit').setAttribute('value', editUser.login);
        document.getElementById('passwordEdit').setAttribute('value', editUser.password);
        if (editUser.roles == "ROLE_ADMIN, ROLE_USER" || editUser.roles == "ROLE_USER, ROLE_ADMIN") {
            document.getElementById('rolesEdit1').setAttribute('selected', 'true');
            document.getElementById('rolesEdit2').setAttribute('selected', 'true');
        } else if (editUser.roles == "ROLE_ADMIN") {
            document.getElementById('rolesEdit1').setAttribute('selected', 'true');
        } else if (editUser.roles == "ROLE_USER") {
            document.getElementById('rolesEdit2').setAttribute('selected', 'true');
        }
        console.log(editUser);
        editModal.show();
    }
    );

    document.getElementById('editUser').addEventListener('submit', submitFormEditUser);
}

function submitFormEditUser(event) {
    event.preventDefault();
    let editUserForm = new FormData(event.target);
    let user = {
        id: editUserForm.get('id'),
        firstName: editUserForm.get('firstName'),
        lastName: editUserForm.get('lastName'),
        passportNumberSeries: editUserForm.get('passportNumberSeries'),
        login: editUserForm.get('login'),
        password: editUserForm.get('password'),
        roles: rolesUser("#rolesEdit")
    }
    console.log(user);
    let request = new Request('http://localhost:8080/api/admin', {
        method: 'PATCH',
        body: JSON.stringify(user),
        headers: {
            'Content-Type': 'application/json',
        },
    });
    fetch(request).then(
        function (response) {
            console.log(response)
            getUsers();
            event.target.reset();
            editModal.hide();
        }
    );
}