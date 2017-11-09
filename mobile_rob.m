% Mobile Robot Simple Simulation
% Developed by Tzivaras Vasilis
% vtzivaras@gmail.com
% Department of Computer Science and Engineering
% University of Ioannina, Robotics Lab
%

% Clear previous simulation data
clear all
clc

% Mobile and enviromental constants
m= 0.2;             % Robot total mass
r = 0.3334;         % Wheel radius
d = 5;              % Robot diameter

omega=[3, 3];
%whre we want ti to go
xdes=5;
ydes=17;
qcur = 0; % Current facing of robot

x_start = 0;
y_start = 0;

v=zeros(1, 2);
v(1, 1) = omega(1)*r;
v(2, 1) = omega(2)*r;

% Simulation start and end time 
startTime = 0;                  % Start time of the simulation
dt = 0.05;   %sqrt(xdes*xdes+ydes*ydes)/500;                     % Steps
endTime = 10;    %sqrt(xdes*xdes+ydes*ydes);                   % End time of the simulationh


times = startTime:dt:endTime;   % Vector with all the times
N = numel(times);               % #of times that simulation will run

% Output values, recorded as the simulation runs
x_out = zeros(2,N);             % mass position
v_out = zeros(2,N);             % mass velocity
a_out = zeros(2,N);             % mass acceleration
q = zeros(3,N);


q(3,1) = qcur;

thetadot = (v(1, 1)+v(2, 1))/d;

sintheta = ydes/(sqrt(xdes*xdes+ydes*ydes));
costheta = xdes/(sqrt(xdes*xdes+ydes*ydes));
index = 1;

tb = 0.1*endTime; % Define the first time checkpoint
tf = endTime; % Define the final time checkpoint

ax= xdes/(tb*tf-tb*tb);
ay= ydes/(tb*tf-tb*tb);

% Loop the hoop
for t = times
	% Accelerate the robot
	if (t <= tb)
		v_out(1, index) = ax*t;
		v_out(2, index) = ay*t;
		
		x_out(1, index) = 0.5*ax*(t*t);
		x_out(2, index) = 0.5*ay*(t*t);
	end
	
	% Smooth straight movement
	if(t >tb && t <= (tf-tb))
		v_out(1, index) = v_out(1, index-1);
		v_out(2, index) = v_out(2, index-1);
		
		x_out(1, index) = 0.5*ax*(tb*tb) + ax*tb*(t-tb);
		x_out(2, index) = 0.5*ay*(tb*tb) + ay*tb*(t-tb);
	end
   
	% Decelarate the robot
   if(t > (tf-tb))
       v_out(1, index) = (ax*(tf-t));
       v_out(2, index) = (ay*(tf-t));
       
       x_out(1, index) = xdes - 0.5*ax*(tf-t)^2;
       x_out(2, index) = ydes - 0.5*ay*(tf-t)^2;
   end
   
   q(1, index) = x_out(1, index);
   q(2, index) = x_out(2, index);
   q(3, index) = atan2(ydes-q(2, index), xdes-q(1, index));


  % x_start, y_start is the robot starting position
  % xdes, ydes is the robot desired position
  % x_out, v_out is vectors with all the information about the robot
  % position and velocities during the simulation.
  mobile_visualization(x_start, y_start, xdes, ydes, q(1, index),q(2, index),q(3, index), x_out, v_out, times);
  
  grid on;
  xlabel('Green circle : START  || Red circle: END');
    
  title('Mobile Robot Simulation');
  
  axis([-5 20 -5 20]);
  axis square;
  pause(0.05);
  
   if((xdes<=x_out(1, index)) && (ydes<=x_out(2, index)))
     break;
   end 
   index = index + 1;
end
% 
% F = mass*sqrt(ax*ax+ay*ay);
% Fl = F/2;
% Fr = F/2;
% 
% % Verify acceleration from the mobile dynamic model
% torquel = Fl*r;
% torquer = Fr*r;
% 
% Iyy = (1/2)*(mass/2)*r*r;
% I = diag([0, Iyy, 0]);
% 
% al = torquel/I(2, 2);
% ar = torquer/I(2, 2);
% atotal = al+ar;
% 
% ax_dyn = atotal*costheta;
% ay_dyn = atotal*sintheta;
