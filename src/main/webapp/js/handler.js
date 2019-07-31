//variables. Pages are in an array.

/* WTF CODE
 var query = "";
 
 
 for (var pair of FD.entries()) {
 query = query + pair[0] + "=" + encodeURI(pair[1]) + "&";
 }
 */

function sendSignupData() {

    event.preventDefault();
    var XHR = new XMLHttpRequest();

    //prepareFormData("signupForm");

    // Bind the FormData object and the form element
    var form = document.getElementById("signupForm");

    var map = new Map();

    var FD = new FormData(form);

    for (var pair of FD.entries()) {
        map[pair[0]] = pair[1];
    }

    // Define what happens on successful data submission
    XHR.addEventListener("load", function (event) {
        var response = event.target.responseText;
        var parsed = JSON.parse(response);
        // if(response === false) --> this does not work. response comes as string.
        if (parsed === false) {   // parsed, so compares as boolean.
            alert("Singup unsuccesful. User already present or need to fill all the required fields.");
        } else {
            alert("Signup successful.");
            hidePages();
            hideNavBars();
            showWelcomePage();
            showWelcomeNavBar();
        }

        //alert(event.target.responseText);
    });

    // Define what happens in case of error
    XHR.addEventListener("error", function (event) {
        alert('Oops! Something went wrong.');
    });


    // Set up our request
    XHR.open("POST", "api/cybersuggest/member/signup"); //relative address
    //XHR.open("POST", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/member/signup"); //absolute address
    XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    // The data sent is what the user provided in the form

    XHR.send("memberObj=" + JSON.stringify(map));
    //XHR.send(query);
}

function sendSuggestionData() {
    event.preventDefault();
    var XHR = new XMLHttpRequest();

    //prepareFormData("addSuggestionForm");
    var form = document.getElementById("addSuggestionForm");

    var map = new Map();

    var FD = new FormData(form);

    for (var pair of FD.entries()) {
        map[pair[0]] = pair[1];
    }


    // test   map["suggestionId"] = 0;

    // Define what happens on successful data submission
    XHR.addEventListener("load", function (event) {
        //alert(event.target.responseText);
        var sugId = event.target.responseText;

        var sugIdStr = sugId.toString(sugId);
        //debugger;

        if (sugId) {
            document.getElementById("suggId").value = sugIdStr;
            document.getElementById("tokenId").value = window.localStorage.getItem("token");
            //document.getElementById("fileUpload").value = window.localStorage.getItem("fileUpload");
            document.getElementById("fileUploadForm").submit();
        } else {
            alert("Please give a title to your proposal.");
        }




        // After suggestion adding and file uploading...

        /*
         hidePages();
         document.getElementById("homePage").style.visibility = "visible";
         */

    });

    // Define what happens in case of error
    XHR.addEventListener("error", function (event) {
        alert('Oops! Something went wrong.');
    });

    // set up the request.
    XHR.open("POST", "api/cybersuggest/suggestion/new"); //relative address
    //XHR.open("POST", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/suggestion/new"); absolute address
    XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    //var data = ("suggestion=" + encodeURI(JSON.stringify(map)) + "&token=" + encodeURI(window.localStorage.getItem("token")));
    var data = ("token=" + encodeURIComponent(window.localStorage.getItem("token")) + "&suggestion=" + encodeURIComponent(JSON.stringify(map)));
    //alert(data);
    XHR.send(data);



}

function login() {

    event.preventDefault();
    var XHR = new XMLHttpRequest();

    //prepareFormData("signupForm");

    // Bind the FormData object and the form element
    var form = document.getElementById("loginForm");

    var map = new Map();

    var FD = new FormData(form);

    for (var pair of FD.entries()) {
        map[pair[0]] = pair[1];
    }

    // Define what happens on successful data submission
    XHR.addEventListener("load", function (event) {
        //alert(event.target.responseText);

        userToken = event.target.responseText;
        window.localStorage.setItem("token", userToken);

        if (userToken === "") {
            alert("Wrong username or password!");
            showWelcomePage();
            showWelcomeNavBar();
        } else if (userToken === "Please fill all the required fields!") {
            alert("Please fill all the required fields!");
            showWelcomePage();
            showWelcomeNavBar();
        } else {
            showHomePage();
            showHomeBar();
            //fetchSuggestions();
        }
    });

    // Define what happens in case of error
    XHR.addEventListener("error", function (event) {
        alert('Failed to login.');
    });

    // Set up our request
    XHR.open("POST", "api/cybersuggest/member/login");
    //XHR.open("POST", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/member/login");
    XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    // The data sent is what the user provided in the form

    XHR.send("memberObj=" + JSON.stringify(map));

}

function logout() {
    var userTokenDefault = window.localStorage.getItem("token");
    //alert(userTokenDefault);

    event.preventDefault();
    var XHR = new XMLHttpRequest();

    XHR.addEventListener("load", function (event) {
        //alert(event.target.responseText);
        alert("Successfully logged out.");

        showWelcomePage();
        showWelcomeNavBar();
    });

    XHR.addEventListener("error", function (event) {
        alert('Failed to logout.');
    });

    XHR.open("POST", "api/cybersuggest/member/logout");
    //XHR.open("POST", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/member/logout");
    XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    XHR.send("token=" + encodeURIComponent(userTokenDefault));
}

/*
 function checkViewAuthorization(token, callback) {
 
 var userToken = encodeURIComponent(window.localStorage.getItem("token"));
 
 event.preventDefault();
 var XHR = new XMLHttpRequest();
 
 XHR.addEventListener("load", function (event) {
 //alert(event.target.responseText);
 response = event.target.responseText;
 
 var viewAuthorized = JSON.parse(response);
 callback(viewAuthorized);
 
 });
 
 XHR.addEventListener("error", function (event) {
 alert("Failed.");
 
 });
 
 XHR.open("GET", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/member/authorization/view?token=" + userToken);
 XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
 XHR.send();
 
 }
 */

function checkAuthorized(token, callback) {
    event.preventDefault();
    var XHR = new XMLHttpRequest();

    var encodedToken = encodeURIComponent(window.localStorage.getItem("token"));

    XHR.addEventListener("load", function (event) {
        //alert(event.target.responseText);
        response = event.target.responseText;

        //var isAuthorized = (response === "true"); 
        var authorizations = JSON.parse(response);

        var lockAuthorized = authorizations[0];
        var deleteAuthorized = authorizations[1];
        var consoleAuthorized = authorizations[2];

        callback(lockAuthorized, deleteAuthorized, consoleAuthorized);

    });

    // Define what happens in case of error
    XHR.addEventListener("error", function (event) {
        alert('Oops! Something went wrong.');
    });

    // Set up our request
    XHR.open("GET", "api/cybersuggest/member/authorization?token=" + encodeURIComponent(window.localStorage.getItem("token")));
    //XHR.open("GET", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/member/authorization?token=" + encodeURIComponent(window.localStorage.getItem("token")));
    XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    // The data sent is what the user provided in the form

    XHR.send();

}


function fetchSuggestions(filter) {


    var cards = document.getElementById("suggestionCards");
    cleanCards(cards);

    if (filter != null)
        window.localStorage.setItem("filter", filter);
    //checkViewAuthorization(window.localStorage.getItem("token"), function (viewAuthorized) {


    checkAuthorized(window.localStorage.getItem("token"), function (lockAuthorized, deleteAuthorized, consoleAuthorized) {

        event.preventDefault();
        var XHR = new XMLHttpRequest();
        // Define what happens on successful data submission
        XHR.addEventListener("load", function (event) {
            //alert(event.target.responseText);           
            userToken = event.target.responseText;
            var cardArray = JSON.parse(userToken);
            //alert(cardArray);


            var navBar = document.getElementById("navBarChoicesHome");
            if (consoleAuthorized) {

                if (navBar.childElementCount < 3) {

                    var adminConsole = document.createElement("a");
                    navBar.appendChild(adminConsole);
                    adminConsole.className = "nav-item nav-link active";
                    adminConsole.setAttribute('onclick', "showAdminConsole();");
                    adminConsole.textContent = "Admin Console";

                    navBar.insertBefore(adminConsole, navBar.childNodes[2]);
                }
            }
            
            //ADD PARAMETER TO NAVBAR METHODS. CHANGE THIS THING ABOVE.

            var size = cardArray.length;
            var i;

            for (i = 0; i < size; i++) {

                var suggestionId = cardArray[i].suggestionId;
                var hasFile = cardArray[i].hasFile;
                var title = cardArray[i].title;
                var isLocked = cardArray[i].isLocked;
                var text = cardArray[i].suggestionText;
                var byUserName = cardArray[i].byUserName;
                var fileIdRef = cardArray[i].fileIdRef;
                var lockedById = cardArray[i].lockedById;

                var cardFrame = document.createElement("div");
                cards.appendChild(cardFrame);
                cardFrame.className = "card border-dark mb-3";
                cardFrame.style = "max-width: 100%;";
                var card = document.createElement("div");
                cardFrame.appendChild(card);
                card.className = "card-body text-dark";
                var header = document.createElement("h5");
                card.appendChild(header);
                var bold = document.createElement("B");
                header.appendChild(bold);
                header.className = "card-title";
                bold.textContent = title;

                var cardText = document.createElement("p");
                card.appendChild(cardText);
                cardText.className = "card-text";
                cardText.textContent = text;

                var byUser = document.createElement("p");
                card.appendChild(byUser);
                var italicUser = document.createElement("I");
                byUser.appendChild(italicUser);
                italicUser.textContent = "By " + byUserName;

                var icons = document.createElement("div");
                card.appendChild(icons);
                icons.className = "btn-container";

                if (hasFile) {
                    var fileAnchor = document.createElement("a");
                    icons.appendChild(fileAnchor);
                    var fileAnchorHref = "DownloadFileServlet?downloadFileId=" + fileIdRef;
                    //var fileAnchorHref = "http://127.0.0.1:8080/suggestion_system_v2-1.0-SNAPSHOT/DownloadFileServlet?downloadFileId=" + fileIdRef;
                    fileAnchor.href = fileAnchorHref;
                    var downloadImage = document.createElement("i");
                    fileAnchor.appendChild(downloadImage);
                    downloadImage.classList.add("fa");
                    downloadImage.classList.add("fa-download");
                }

                if (lockAuthorized === "lock") {
                    var lockAnchor = document.createElement("a");
                    icons.appendChild(lockAnchor);
                    // call a service to change the lock status here.
                    var lockImage = document.createElement("i");
                    lockAnchor.appendChild(lockImage);
                    if (isLocked) {
                        lockImage.classList.add("fa");
                        lockImage.classList.add("fa-lock");
                    } else {
                        lockImage.classList.add("fa");
                        lockImage.classList.add("fa-unlock");
                    }
                    //debugger;
                    /*
                     var suggIdStr ="" + suggestionId;
                     var funcSignature = "lockService(" + suggIdStr + "," + filter + ")";
                     lockImage.setAttribute('onclick', funcSignature);
                     */
                    lockImage.setAttribute('onclick', "lockService('" + suggestionId + "');");
                }

                if (deleteAuthorized === "delete") {
                    var garbageAnchor = document.createElement("a");
                    icons.appendChild(garbageAnchor);
                    // call a service to change the lock status here.
                    var garbageImage = document.createElement("i");
                    garbageAnchor.appendChild(garbageImage);
                    garbageImage.classList.add("fa");
                    garbageImage.classList.add("fa-trash");
                    garbageImage.setAttribute('onclick', "deleteService('" + suggestionId + "');");
                }

                if (isLocked) {
                    var lockedBy = document.createElement("p");
                    card.appendChild(lockedBy);
                    var italicLockedBy = document.createElement("I");
                    lockedBy.appendChild(italicLockedBy);
                    italicLockedBy.textContent = "Locked by " + lockedById;
                }
            }
        });
        // Define what happens in case of error
        XHR.addEventListener("error", function (event) {
            alert('Oops! Something went wrong.');
        });

        if (filter == null) {
            XHR.open("GET", "api/cybersuggest/suggestion/fetchSuggestions?token=" + encodeURIComponent(window.localStorage.getItem("token")));
            //XHR.open("GET", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/suggestion/fetchSuggestions?token=" + encodeURIComponent(window.localStorage.getItem("token")));
        } else {
            XHR.open("GET", "api/cybersuggest/suggestion/filterSuggestions?token=" + encodeURIComponent(window.localStorage.getItem("token")) + "&filter=" + encodeURIComponent(filter));
            //XHR.open("GET", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/suggestion/filterSuggestions?token=" + encodeURIComponent(window.localStorage.getItem("token")) + "&filter=" + encodeURIComponent(filter));
        }
        XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        XHR.send();
    });
    //});
}

function fetchMembers() {

    var cards = document.getElementById("memberCards");
    cleanCards(cards);

    event.preventDefault();
    var XHR = new XMLHttpRequest();

    XHR.addEventListener("load", function (event) {
        //fetchMembers


        var responseData = event.target.responseText;
        var memberArray = JSON.parse(responseData);

        var size = memberArray.length;
        var i;

        for (i = 0; i < size; i++) {

            var userName = memberArray[i].userName;
            var email = memberArray[i].email;
            var password = memberArray[i].password;
            var name = memberArray[i].name
            var surname = memberArray[i].surname;
            var userType = memberArray[i].userType;

            var cardFrame = document.createElement("div");
            cards.appendChild(cardFrame);
            cardFrame.className = "card border-dark mb-3";
            //cardFrame.style = "max-width: 100%;";
            var card = document.createElement("div");
            cardFrame.appendChild(card);
            card.className = "card-body text-dark";
            var header = document.createElement("h5");
            card.appendChild(header);
            var bold = document.createElement("B");
            header.appendChild(bold);
            header.className = "card-title";
            bold.textContent = name + " " + surname;

            var userNameLabel = document.createElement("LABEL");
            card.appendChild(userNameLabel);
            userNameLabel.setAttribute("for", "Username");
            var userNameInfo = document.createElement("p");
            card.appendChild(userNameInfo);
            var userNameLabelText = document.createTextNode("Username: ");
            userNameLabel.appendChild(userNameLabelText);
            userNameInfo.className = "card-text";
            userNameInfo.textContent = userName;

            var passwordLabel = document.createElement("LABEL");
            card.appendChild(passwordLabel);
            passwordLabel.setAttribute("for", "Password");
            var passwordInfo = document.createElement("p");
            card.appendChild(passwordInfo);
            var passwordLabelText = document.createTextNode("Password: ");
            passwordLabel.appendChild(passwordLabelText);
            passwordInfo.className = "card-text";
            passwordInfo.textContent = password;

            var emailLabel = document.createElement("LABEL");
            card.appendChild(emailLabel);
            emailLabel.setAttribute("for", "Email");
            var emailInfo = document.createElement("p");
            card.appendChild(emailInfo);
            var emailInfoLabelText = document.createTextNode("Email: ");
            emailLabel.appendChild(emailInfoLabelText);
            emailInfo.className = "card-text";
            emailInfo.textContent = email;

            var userTypeLabel = document.createElement("LABEL");
            card.appendChild(userTypeLabel);
            userTypeLabel.setAttribute("for", "User Type");
            var userTypeInfo = document.createElement("p");
            card.appendChild(userTypeInfo);
            var userTypeInfoLabelText = document.createTextNode("User Type: ");
            userTypeLabel.appendChild(userTypeInfoLabelText);
            userTypeInfo.className = "card-text";
            userTypeInfo.textContent = userType;

            var changeUserTypeBtn = document.createElement("BUTTON");
            card.appendChild(changeUserTypeBtn);
            changeUserTypeBtn.setAttribute('onclick', "changeMemberType('" + userName + "');");
            changeUserTypeBtn.className = "btn btn-outline-dark";
            changeUserTypeBtn.textContent = "Change user type";
            /*
             var byUser = document.createElement("p");
             card.appendChild(byUser);
             var italicUser = document.createElement("I");
             byUser.appendChild(italicUser);
             italicUser.textContent = "By " + byUserName;
             */
        }

    });

    XHR.addEventListener("error", function (event) {
        alert("Failed");
    });

    XHR.open("GET", "api/cybersuggest/member/admin/fetch?token=" + encodeURIComponent(window.localStorage.getItem("token")));
    //XHR.open("GET", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/member/admin/fetch?token=" + encodeURIComponent(window.localStorage.getItem("token")));
    XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    XHR.send();

}

function changeMemberType(memberUsername) {
    event.preventDefault();
    var XHR = new XMLHttpRequest();

    var userToken = encodeURIComponent(window.localStorage.getItem("token"));

    XHR.addEventListener("load", function (event) {
        fetchMembers();

    });

    XHR.addEventListener("error", function (event) {
        alert('Failed.');
    });
    
    XHR.open("POST", "api/cybersuggest/member/admin/changeType");
    //XHR.open("POST", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/member/admin/changeType");
    XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    var sendData = "token=" + userToken + "&userName=" + encodeURIComponent(memberUsername);
    XHR.send(sendData);
}

function lockService(suggId) {

    event.preventDefault();
    var XHR = new XMLHttpRequest();

    var filter = window.localStorage.getItem("filter");

    XHR.addEventListener("load", function (event) {
        //alert(event.target.responseText);
        //setLocked
        if (filter !== "") {
            fetchSuggestions(filter);
        } else {
            fetchSuggestions();
        }

    });

    // Define what happens in case of error
    XHR.addEventListener("error", function (event) {
        alert('Oops! Something went wrong.');
    });


    XHR.open("POST", "api/cybersuggest/suggestion/changeLockStatus");
    //XHR.open("POST", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/suggestion/changeLockStatus");
    XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    // The data sent is what the user provided in the form

    var data = ("token=" + encodeURIComponent(window.localStorage.getItem("token")) + "&suggestionId=" + encodeURIComponent(suggId));
    //alert(data);
    XHR.send(data);

}

function deleteService(suggId) {
    event.preventDefault();
    var XHR = new XMLHttpRequest();

    XHR.addEventListener("load", function (event) {
        fetchSuggestions();
    });

    // Define what happens in case of error
    XHR.addEventListener("error", function (event) {
        alert('Oops! Something went wrong.');
    });

    XHR.open("POST", "api/cybersuggest/suggestion/delete");
    //XHR.open("POST", "http://localhost:8080/suggestion_system_v2-1.0-SNAPSHOT/api/cybersuggest/suggestion/delete");
    XHR.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    var data = ("token=" + encodeURIComponent(window.localStorage.getItem("token")) + "&suggestionId=" + encodeURIComponent(suggId));
    XHR.send(data);

}

function openSignupPage() {
    hidePages();

    document.getElementById("signupPage").style.visibility = "visible";

}

function hidePages() {
    var pages = document.getElementsByClassName("page");

    for (var i = 0, max = pages.length; i < max; i++) {
        pages[i].style.visibility = "hidden";
    }
}

function hideNavBars() {
    var navBars = document.getElementsByClassName("navBar");

    for (var i = 0, max = navBars.length; i < max; i++) {
        navBars[i].style.visibility = "hidden";
    }
}

function showWelcomeNavBar() {
    hideNavBars();
    document.getElementById("navBarWelcome").style.visibility = "visible";
}

function showHomeBar() {
    hideNavBars();
    document.getElementById("navBarHome").style.visibility = "visible";
}


function openAddSuggestionPage() {
    hidePages();
    document.getElementById("addSuggestionPage").style.visibility = "visible";
}

function showWelcomePage() {
    hidePages();
    document.getElementById("welcomePage").style.visibility = "visible";
}

function showHomePage() {
    hidePages();
    document.getElementById("homePage").style.visibility = "visible";
    fetchSuggestions();
}

function cleanCards(cards) {
    var allCards = cards.getElementsByClassName("card");

    for (var i = allCards.length - 1; i >= 0; i--) {
        cards.removeChild(allCards[i]);
    }
}

function showAdminConsole() {
    hidePages();
    document.getElementById("adminConsolePage").style.visibility = "visible";
    fetchMembers();

}

// Access the form element...

// ...and take over its submit event.

/*
 form.addEventListener("submit", function (event) {
 event.preventDefault();
 
 sendData();
 });
 
 */
