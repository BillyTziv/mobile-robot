%x,y kentro tou kyklou
%theta o prosanatolismos tou kuklou
function mobile_orientation(x,y,theta, xdes, ydes)
% x=1;
% y=1;
% theta=pi/4;
z=[xdes, ydes, 0];
r=1; %aktina tou kuklou(an thelete tin pername kai orisma)
%hold on
%ftiaxnei ton kuklo
th = 0:pi/50:2*pi;
x_circle = r * cos(th) + x;
y_circle = r * sin(th) + y;
%ftiaxnei ti grammh
x_line=[x, x + (r*cos(theta))];
y_line=[y, y + (r*sin(theta))];

x1 = r * cos(th) + xdes;
y2 = r * sin(th) + ydes;
plot(x1, y2, 'g', 'LineWidth', 7);

hold on;
plot(x_circle, y_circle, x_line, y_line, 'LineWidth', 7);
hold off;


%h = [x_circle, y_circle, x_line, y_line];
%hold off
%end