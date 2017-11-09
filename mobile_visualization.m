function h = mobile_visualization(x_start, y_start, x_end, y_end, xpos, ypos, theta, x_out, v_out, times)
    % Create a position graph
    subplot(2,2,2)
    ylabel('position(m)');
    xlabel('time (s)');
    axis([0 10 0 20]);
    plot(times, x_out, 'b', 'LineWidth', 2)
    title('Robot Position')
    hold on;
    
    % Create a velocity graph
    subplot(2,2,4)
    axis([0 10 0 5]);
    ylabel('velocity(m/s^2)');
    xlabel('time (s)');
    plot(times, v_out, 'g', 'LineWidth', 2)
    title('Robot Velocity')
    hold on;
    
    r=1;
    th = 0:pi/50:2*pi;
    x_circle = r * cos(th) + xpos;
    y_circle = r * sin(th) + ypos;

    x_line = [xpos, xpos + (r*cos(theta))];
    y_line = [ypos, ypos + (r*sin(theta))];

    % Creating the start point (green color)
    x_start = r * cos(th) + x_start;
    y_start = r * sin(th) + y_start;
    subplot(2,2,[1,3])
    plot(x_start, y_start, 'g', 'LineWidth', 2);
    hold on;
    
    % Creating the end point (red color)
    x_end = r * cos(th) + x_end;
    y_end = r * sin(th) + y_end;
    plot(x_end, y_end, 'r', 'LineWidth', 2);
    hold on;
    title('Robot movement in 2D')

    % Creating the start point (green color)
    h = plot(x_circle, y_circle, x_line, y_line, 'LineWidth', 2);
    hold off;
end