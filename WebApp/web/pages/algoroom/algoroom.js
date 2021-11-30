
var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var USER_FILE_URL = buildUrlWithContextPath("fileslist");
var USER_PDATA_URL = buildUrlWithContextPath("pdata");
var file;



//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(users) {
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, username) {
        console.log("Adding user #" + index + ": " + username);

        //create a new <li> tag with a value in it and append it to the #userslist (div with id=userslist) element
        $('<li>' + username + '</li>')
            .appendTo($("#userslist"));
    });
}

function refreshDataList(data) {
    //clear all current users
    $("#pdata").empty().append('<p>' + data + '</p>');

}



function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function(users) {
            refreshUsersList(users);
        }
    });
}


function ajaxPdata() {
    $.ajax({
        url: "../../pages/algoroom/data",
        success: function(data) {
            refreshDataList(data);
        }
    });
}



//activate the timer calls after the page is loaded
$(function() {

    //The users list /files list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);


});


$(function()
{

    //For "UPLOAD" Button we present + save the file.
    $('#file-load').change(function () {
        file = $('#file-load')[0].files[0];
        $('#selected_filename').text(file.name);
    });

    $("#fileform").submit(function(e) {
        e.preventDefault();
        var formData = new FormData(this);

        $.ajax({
            url: this.action,
            type: 'POST',
            data: formData,
            statusCode: {
                200: function() {
                    alert("File uploaded successfully.")
                },
                406: function (){
                    alert("File is corrupted or invalid.")
                },
                409:function(){
                    alert("user exceeded 2 files limit.");
                }
            },
            cache: false,
            contentType: false,
            processData: false
        });
        file=null;
        $('#selected_filename').text("");
        return false;
    });

    $("#pform").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function(errorObject) {
                console.error("Failed to login !");
                $("#error-placeholder").append(errorObject.responseText)
            },
            success: function(nextPageUrl) {
                window.location.replace(nextPageUrl);
            }
        });


        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });

    $("#mutation").submit(function(e) {
        e.preventDefault();

        $.ajax({
            url: "../../pages/algoroom/data",
            type: 'POST',
            data: $(this).serialize(),
            statusCode: {
                200: function() {
                    ajaxPdata()
                },
                406: function (){
                    alert("File is corrupted or invalid.")
                },
                409:function(){
                    alert("user exceeded 2 files limit.");
                }
            },

        });
    });


    const selection = document.getElementsByClassName('Selection');
    const TrunctionBtn = document.getElementById('Trunction');
    const RouletteBtn = document.getElementById('Rw');
    const TournamentBtn = document.getElementById('Tournament');

    let selectionArray = document.querySelectorAll('.Selection');
    let initialPopulation = document.querySelector('.Population');
    let runAlgo = document.querySelector('.Run');
    selectionArray.forEach(function(elem) {
        elem.addEventListener("click", function() {
            initiate(elem.getAttribute('name'));
            selectionArray.forEach(function(elem2){
                elem2.setAttribute("style", "background-color: default;");
            })
            elem.setAttribute("style", "background-color: green;");
        });
    });

    runAlgo.addEventListener("click",function(){
        initiate(runAlgo.getAttribute('name'));
    });

    $("#Population").submit(function(e) {
        e.preventDefault();

        $.ajax({
            url: "../../pages/algoroom/data",
            type: 'POST',
            data: $(this).serialize(),
            success: function(result){
                ajaxPdata();
            }
        });
    });

    $("#Elitism").submit(function(e) {
        e.preventDefault();
        $.ajax({
            url: "../../pages/algoroom/data",
            type: 'POST',
            data: $(this).serialize(),
            success: function(result){
                ajaxPdata();
            }
        });
    });

    $("#Crossover").submit(function(e) {
        e.preventDefault();
        $.ajax({
            url: "../../pages/algoroom/data",
            type: 'POST',
            data: $(this).serialize(),
            success: function(result){
                ajaxPdata();
            }
        });
    });




    function initiate(data) {
        $.ajax({ type: "POST",
            url: "../../pages/algoroom/data",
            data: data,
            success: function(result){
                ajaxPdata();
            },
            error: function (e) {
                console.log("ERROR: ", e);
            },
            done: function (e) {
                console.log("DONE");
            }

        });
    }


});




