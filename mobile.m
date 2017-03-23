clear all
clc
%figure;

mass = 0.2;
r=0.3334;
d=1;
omega=[3, 4];
%whre we want ti to go
xdes=19;
ydes=22;
xstart = 0;                     % Start point of x axis
ystart = 0;    
qcur = 0; % Current facing of robot

% a = 10;

v=zeros(1, 2);
v(1, 1) = omega(1)*r;
v(2, 1) = omega(2)*r;

% Simulation start and end time 
startTime = 0;                  % Start time of the simulation
% dt = sqrt(xdes*xdes+ydes*ydes)/500;                     % Steps
% endTime = sqrt(xdes*xdes+ydes*ydes);                   % End time of the simulationh
dt = 0.01;
endTime = 15;
% sqrt(xdes*xdes+ydes*ydes)*dt
times = startTime:dt:endTime;   % Vector with all the times
N = numel(times);               % #of times that simulation will run

% Output values, recorded as the simulation runs
x_out = zeros(2,N);             % mass position
v_out = zeros(2,N);             % mass velocity
a_out = zeros(2,N);             % mass acceleration
q = zeros(3,N);
q(3,1) = qcur;

%v_total_new = (v(1, 1)+v(2, 1))/2;

% thetadot = (v(1, 1)+v(2, 1))/d;

index = 1;
count = 1;

tb = 0.1*endTime;
tf = endTime;

ax= xdes/(tb*tf-tb*tb);
ay= ydes/(tb*tf-tb*tb);

for t = times
   if (t <= tb) % tb einai to telos ths epitaxynomenhs
       v_out(1, index) = ax*t;
       v_out(2, index) = ay*t;
       x_out(1, index) = 0.5*ax*(t*t);
       x_out(2, index) = 0.5*ay*(t*t);
       count = count + 1;
   end    
    if(t >tb && t <= (tf-tb)) % diarkeia omalhs kinhsh
       v_out(1, index) = v_out(1, index-1);
       v_out(2, index) = v_out(2, index-1);
       x_out(1, index) = 0.5*ax*(tb*tb) + ax*tb*(t-tb);
       x_out(2, index) = 0.5*ay*(tb*tb) + ay*tb*(t-tb);
       count = count + 1;
    end
    if(t > (tf-tb)) % epivradynomenh
       v_out(1, index) = ax*(tf-t);
       v_out(2, index) = ay*(tf-t);
       x_out(1, index) = xdes - 0.5*ax*(tf-t)^2;
       x_out(2, index) = ydes - 0.5*ay*(tf-t)^2;
    end
   
    q(3, index) = atan2(x_out(2, index), x_out(1, index));
    q(1, index) = x_out(1, index);
    q(2, index) = x_out(2, index);
    
    mobile_orientation(xstart, ystart, xdes, ydes, q(1, index),q(2, index),q(3, index));
    grid on;
    %plot (h);
    axis([0 50 0 50]);
    axis square;
    pause(0.01);
  
  
%     if((xdes<=x_out(1, index)) && (ydes<=x_out(2, index)))
%         break;
%     end
    index = index + 1;
end