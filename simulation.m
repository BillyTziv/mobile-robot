% Simulation code for mobile robot

% Constants
m = 2;                          % Mass
L = 10;                         % Length
omega = [5, 5];                 % Angular velocity of the two motors
I = diag([5e-3, 5e-3]);         % Inertia Matrix
q = zeros(3, 1);                % Position (x, y) and angle (theta)

% Rotation matrix
theta = q(3);
R(:, 1) = [-cos(theta), sin(theta), 0];
R(:, 2) = [-sin(theta), 0, 0];
R(:, 3) = [cos(theta), 0, 1];

%  Calculate velocities
vr = omega(1)*(R+1/2);
vl = omega(2)*(R+1/2);
vtotal = (vr+vl)/2;

% Calculate the KInetic Energy (Dynamic is zero)
K = 0.5*m*vtotal*vtotal;

G = 0.5*I*omega.^2';

% Simulation start and end time 
startTime = 0;                  % Start time of the simulation
endTime = 5;                    % End time of the simulationh
dt = 0.001;                     % Steps
times = startTime:dt:endTime;   % Vector with all the times
N = numel(times);               % #of times that simulation will run

index = 1;
for t = times
    xdot = vtotal*cos(theta);
    ydot = vtotal*sin(theta);
    thetadot = omega;
    
    % Missing the periorismos
    % ...
    
    % Langrange equetion due to kinetic energy and rotarion
    L = [K(1) K(2)]' + G;
end

% Create a new figure and visualize the simulation
figure;
for index = 1:40:length(times)
    visualize(x_out(1, index), x_out(2, index), x_out(3, index));
    drawnow;
    if index < length(t_sim)
        clf;
    end
end
