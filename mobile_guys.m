clear all
clc


mass = 0.2;
r = 0.3334;
d = 0.5;
L = 0.5;
diag_i = [0 0 ((mass*(L*L+d*d))/12)];
I = diag(diag_i);
omega = [3, 3];

xstart = 0;
ystart = 0;


%whre we want ti to go
xdes=45;
ydes=30;
qcur = 0; % Current facing of robot

% a = 10;

v=zeros(1, 2);
v(1, 1) = omega(1)*r;
v(2, 1) = omega(2)*r;
fl=0;
% Simulation start and end time 
startTime = 0;                  % Start time of the simulation
% dt = sqrt(xdes*xdes+ydes*ydes)/500;                     % Steps
%endTime = sqrt(xdes*xdes+ydes*ydes);                   % End time of the simulationh
endTime = 5;
dt = 0.01;
% endTime = 5;
% sqrt(xdes*xdes+ydes*ydes)*dt
times = startTime:dt:endTime;   % Vector with all the times
N = numel(times);               % #of times that simulation will run

% Output values, recorded as the simulation runs
x_out = zeros(2,N);             % mass position
v_out = zeros(2,N);             % mass velocity
a_out = zeros(2,N);             % mass acceleration

%a_out(:, :) = [3, 3];
q = zeros(3,N);
q(3,1) = qcur;

%v_total_new = (v(1, 1)+v(2, 1))/2;

thetadot = (v(1, 1)+v(2, 1))/d;

index = 1;

tb = 0.1*endTime;
tf = 0.7*endTime;

ax= xdes/(tb*tf-tb*tb);
ay= ydes/(tb*tf-tb*tb);
% ax = 0.8;
% ay = 0.8;


flag=0;
for t = times
   % Increasing Motion
   if (t <= tb)
       v_out(1, index) = ax*t;
       v_out(2, index) = ay*t;
       
       x_out(1, index) = 0.5*ax*(t*t);
       x_out(2, index) = 0.5*ay*(t*t);
       l_inc_v = index;
   end
   
   
   % Normal Motion
   if(t > tb && t <= tf)
       
       v_out(1, index) = v_out(1, l_inc_v);
       v_out(2, index) = v_out(2, l_inc_v);
       
       %x_out(1, index) = 0.5*ax*(tb*tb) + ax*tb*(t-tb);
       %x_out(2, index) = 0.5*ay*(tb*tb) + ay*tb*(t-tb);
     
       x_out(1, index) = x_out(1, index-1) + v_out(1, l_inc_v) * (dt);
       x_out(2, index) = x_out(2, index-1) + v_out(2, l_inc_v) * (dt);

       l_norm_index = index;
   end
   
   % Decreasing Motion
   if(t > tf)
       %v_out(1, index) = ax*(tf-t);
       %v_out(2, index) = ay*(tf-t);
       a=234234234
       v_out(1, index) = v_out(1, l_inc_v) - ax*(t-tf);
       v_out(2, index) = v_out(2, l_inc_v) - ay*(t-tf);
       %v_out(1, index) = 5;
       %v_out(2, index) = 5;
       
       %x_out(1, index) = xdes - 0.5*ax*(tf-t)^2;
       %x_out(2, index) = ydes - 0.5*ay*(tf-t)^2;
       t=t-tf;
     
            x_out(1, index) = x_out(1, l_norm_index) + v_out(1, l_inc_v)*t - 0.5*ax*t*t;
            x_out(2, index) = x_out(2, l_norm_index) + v_out(2, l_inc_v)*t - 0.5*ay*t*t;
          
       
    end
   
    q(3, index) = atan2(x_out(2, index), x_out(1, index));
    q(1, index) = x_out(1, index);
    q(2, index) = x_out(2, index);
    
    % Thetadot is zero caz we have no rotation
    %L = 0.5*mass*(v_out(1, index)*v_out(1, index)+v_out(2, index)*v_out(2, index));
    
    mobile_orientation(xstart, ystart, xdes, ydes,q(1, index),q(2, index),q(3, index));
    grid on;
    title('Mobile Simulation');
    xlabel('X Pos');
    ylabel('Y Pos');
    %plot (h);
    axis([-5 50 -5 50]);
    axis square;
    pause(0.01);
  
  
    if((xdes<=x_out(1, index)) && (ydes<=x_out(2, index)))
       break;
    end
    index = index + 1;
end

% figure;
% 
% axis([-5 50 -5 50]);
% plot(times, x_out, 'LineWidth', 5)
% grid on;