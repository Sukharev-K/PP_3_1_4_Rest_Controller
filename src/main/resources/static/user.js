fetch("http://localhost:8080/api/user")
    .then(res => res.json())
    .then(data => {
        console.log(data);
        showOneUser(data);
    })


function showOneUser(user) {

    let temp = "";
    temp += "<tr>";
    temp += "<td>" + user.id + "</td>";
    temp += "<td>" + user.firstName + "</td>";
    temp += "<td>" + user.lastName + "</td>";
    temp += "<td>" + user.passportNumberSeries + "</td>";
    temp += "<td>" + user.login + "</td>";
    temp += "<td>" + user.role + "</td>";
    //temp += "<td>" + user.roles.map(role=>role.roleName.substring(5)) + "</td>"
    temp += "</tr>"

    document.getElementById("oneUser").innerHTML = temp;
}