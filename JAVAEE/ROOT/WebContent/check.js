function loginCheck() {
	var pass = document.getElementById("pass").value;
	var username = document.getElementById("usr").value;

	var event = event || window.event;
	// username
	var pattern1 = /^[a-zA-Z][a-zA-Z\d_]*[a-zA-Z\d_]$/;
	// username-caps
	var pattern2 = /.*[A-Z].*/;
	// pass
	var pattern3 = /[a-zA-Z\d_].*[a-zA-Z\d_]$/;

	// username
	if (typeof username == "undefined" || username == null || username == "") {
		alert("请输入用户名！");
		event.preventDefault(); // 兼容标准浏览器
	} else if (username.length < 5 || username.length > 10) {
		alert("用户名长度需5-10位");
		event.preventDefault(); // 兼容标准浏览器
	} else if (!pattern1.test(username)) {
		alert("用户名需要以英文字母开头，由英文字母数字和_组成");
		event.preventDefault(); // 兼容标准浏览器
	} else if (!pattern2.test(username)) {
		alert("用户名必须包含至少一个大写字母");
		event.preventDefault(); // 兼容标准浏览器
	}

	// pass
	else if (typeof pass == "undefined" || pass == null || pass == "") {
		alert("密码不能为空");
		event.preventDefault(); // 兼容标准浏览器
	} else if (pass.length > 12 || pass.length < 6) {
		alert("密码长度需6-12位");
		event.preventDefault(); // 兼容标准浏览器
	} else if (!pattern3.test(pass)) {
		alert("密码应由英文字母数字和_组成");
		event.preventDefault(); // 兼容标准浏览器
	}
}

// 修改密码合法性检验
function registerCheck() {
	var pass = document.getElementById("pass").value;
	var username = document.getElementById("usr").value;
	var name = document.getElementById("name").value;
	var age = document.getElementById("age").value;
	var tele = document.getElementById("tele").value;
	var repass = document.getElementById("repeatpass").value;

	var event = event || window.event;
	// username
	var pattern1 = /^[a-zA-Z][a-zA-Z\d_]*[a-zA-Z\d_]$/;
	// username-caps
	var pattern2 = /.*[A-Z].*/;
	// pass
	var pattern3 = /[a-zA-Z\d_].*[a-zA-Z\d_]$/;
	// age
	var pattern4 = /^([1-9]\d|\d)$/;
	// tele
	var pattern5 = "^((13[0-9])|(14[4-9])|(15[^4])|(16[6-7])|(17[^9])|(18[0-9])|(19[1|8|9]))\d{8}$";

	// username
	if (typeof username == "undefined" || username == null || username == "") {
		alert("请输入用户名！");
		event.preventDefault(); // 兼容标准浏览器
	} else if (username.length < 5 || username.length > 10) {
		alert("用户名长度需5-10位");
		event.preventDefault(); // 兼容标准浏览器
	} else if (!pattern1.test(username)) {
		alert("用户名需要以英文字母开头，由英文字母数字和_组成");
		event.preventDefault(); // 兼容标准浏览器
	} else if (!pattern2.test(username)) {
		alert("用户名必须包含至少一个大写字母");
		event.preventDefault(); // 兼容标准浏览器
	}

	// name
	else if (typeof name == "undefined" || name == null || name == "") {
		alert("名字不能为空");
		event.preventDefault(); // 兼容标准浏览器
	}

	// age
	else if (!pattern4.test(age)
			&& !(age == "undefined" || age == null || age == "")) {
		alert("年龄只能为0-99的整数");
		event.preventDefault(); // 兼容标准浏览器
	}

	// tele
	else if (!pattern5.test(tele)
			&& !(tele == "undefined" || tele == null || tele == "")) {
		alert("请输入正确的电话号码");
		event.preventDefault(); // 兼容标准浏览器
	}

	// pass
	else if (typeof pass == "undefined" || pass == null || pass == "") {
		alert("密码不能为空");
		event.preventDefault(); // 兼容标准浏览器
	} else if (pass.length > 12 || pass.length < 6) {
		alert("密码长度需6-12位");
		event.preventDefault(); // 兼容标准浏览器
	} else if (!pattern3.test(pass)) {
		alert("密码应由英文字母数字和_组成");
		event.preventDefault(); // 兼容标准浏览器
	}

	// repeat pass
	else if (pass != repass) {
		alert("密码输入不一致");
		event.preventDefault(); // 兼容标准浏览器
	}
}

function sendCheck() {
	var username = document.getElementById("username").value;
	var teleno = document.getElementById("teleno").value;
	var event = event || window.event;

	if (typeof username == "undefined" || username == null || username == "") {
		alert("请输入用户名！");
		event.preventDefault(); // 兼容标准浏览器
	} else if (typeof teleno == "undefined" || teleno == null || teleno == "") {
		alert("输入手机号！");
		event.preventDefault(); // 兼容标准浏览器
	}
}

function resetCheck() {
	var username = document.getElementById("username1").value;
	var code = document.getElementById("code").value;
	var pass = document.getElementById("password").value;
	var repass = document.getElementById("repeatpass").value;
	var pattern3 = /[a-zA-Z\d_].*[a-zA-Z\d_]$/;
	var event = event || window.event;

	if (typeof username == "undefined" || username == null || username == "") {
		alert("请输入用户！");
		event.preventDefault(); // 兼容标准浏览器
	} else if (typeof code == "undefined" || code == null || code == "") {
		alert("请输入验证码！");
		event.preventDefault(); // 兼容标准浏览器
	} else if (typeof pass == "undefined" || pass == null || pass == "") {
		alert("密码不能为空");
		event.preventDefault(); // 兼容标准浏览器
	} else if (pass.length > 12 || pass.length < 6) {
		alert("密码长度需6-12位");
		event.preventDefault(); // 兼容标准浏览器
	} else if (!pattern3.test(pass)) {
		alert("密码应由英文字母数字和_组成");
		event.preventDefault(); // 兼容标准浏览器
	} else if (pass != repass) {
		alert("密码输入不一致");
		event.preventDefault(); // 兼容标准浏览器
	}
}