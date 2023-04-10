var canvas = document.getElementById("canvas");
		var ctx = canvas.getContext("2d");

		ctx.strokeStyle = '#00ffff';
		ctx.lineWidth = 10; //17
		ctx.shadowBlur= 9;
		ctx.shadowColor = '#00ffff'

		function degToRad(degree){
			var factor = Math.PI/180;
			return degree*factor;
		}


		const options = {
          weekday: "long",
          year: "numeric",
          month: "long",
          day: "numeric",
        };

		function renderTime(){

		    // Get the computed value of the CSS variable
                  const root = document.documentElement;
                  const styles = window.getComputedStyle(root);
                  const bgColor = styles.getPropertyValue('--bg');
                  const compColor = styles.getPropertyValue('--comp');

			var now = new Date();
			var today = now.toLocaleDateString("bg", options).split(',');
			var time = now.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit', hour12: false})
			var hrs = now.getHours();
			var min = now.getMinutes();
			var sec = now.getSeconds();
			var mil = now.getMilliseconds();
			var smoothsec = sec+(mil/1000);
            var smoothmin = min+(smoothsec/60);

			//Background
			gradient = ctx.createRadialGradient(200, 200, 5, 200, 200, 200);
			gradient.addColorStop(0, "#03303a");
			gradient.addColorStop(1, bgColor);
			ctx.fillStyle = gradient;
			//ctx.fillStyle = 'rgba(00 ,00 , 00, 1)';
			ctx.fillRect(0, 0, 400, 400);
			//Hours
			ctx.beginPath();
			ctx.arc(200,200,170, degToRad(270), degToRad((hrs*30)-90));
			ctx.stroke();
			//Minutes
			ctx.beginPath();
			ctx.arc(200,200,150, degToRad(270), degToRad((smoothmin*6)-90));
			ctx.stroke();
			//Seconds
			ctx.beginPath();
			ctx.arc(200,200,130, degToRad(270), degToRad((smoothsec*6)-90));
			ctx.stroke();
			//Time
            ctx.font = "85px Helvetica Bold";
            ctx.fillStyle = 'rgba(255, 255, 255, 1)';
            ctx.fillText(time, 95, 210);
			//Date
			ctx.font = "23px Helvetica";
			ctx.fillStyle = 'rgba(255, 255, 255, 1)'
			ctx.fillText(today[1], 105, 245);
			//Day
            ctx.font = "23px Helvetica";
            ctx.fillStyle = 'rgba(255, 255, 255, 1)'
            ctx.fillText(today[0], 135, 270);

		}
		setInterval(renderTime, 40);