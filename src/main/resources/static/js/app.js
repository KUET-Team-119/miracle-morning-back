const signUpButton = document.getElementById("signUp");
const signInButton = document.getElementById("signIn");
const container = document.getElementById("container");
const password = document.getElementById("password");
const generatePasswordBtn = document.getElementById("generatePassword");

signUpButton.addEventListener("click", () => {
  container.classList.add("right-panel-active");
});

signInButton.addEventListener("click", () => {
  container.classList.remove("right-panel-active");
});

generatePasswordBtn.addEventListener("click", () => {
	const characters = "abcdefghijklmnopqrstuvwxyz";
	const randomNum = Math.floor(Math.random() * 10).toString();
	let randomChar = '';
	    
	for (let i = 0; i < 4; i++) {
	    randomChar += characters.charAt(Math.floor(Math.random() * characters.length));
	}
	
	const randomStr = randomNum + randomChar;
	password.value = randomStr;
});
