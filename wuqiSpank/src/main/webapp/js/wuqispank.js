			function fnMessage ( sMesg )
			{
				var n = document.getElementById('info');
				n.innerHTML = sMesg+"<br>";
				n.scrollTop = n.scrollHeight;
			}
			function getText(el) {
			
				if (el.textContent) return el.textContent;
				if (el.innerText) return el.innerText;
				return el.innerHTML.replace(/<[^>]*>/g,'');
			}			
   	function selectText(containerid) {
        if (document.selection) {
            var range = document.body.createTextRange();
            range.moveToElementText(document.getElementById(containerid));
            range.select();
        } else if (window.getSelection) {
            var range = document.createRange();
            range.selectNode(document.getElementById(containerid));
            window.getSelection().addRange(range);
        }
    }			
