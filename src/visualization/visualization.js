function parseText(text) {
    console.log(text);
}

var openFile = function(event) {
    var input = event.target;

    var reader = new FileReader();
    var text;
    reader.onload = function(){
        text = reader.result;
        parseText(text);
    };
    reader.readAsText(input.files[0]);
};