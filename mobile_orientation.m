function h = mobile_orientation(x_start, y_start, x_end, y_end, xpos, ypos, theta)
    r=1;
   
    th = 0:pi/50:2*pi;
    x_circle = r * cos(th) + xpos;
    y_circle = r * sin(th) + ypos;
    %ftiaxnei ti grammh
    x_line = [xpos, xpos + (r*cos(theta))];
    y_line = [ypos, ypos + (r*sin(theta))];

    
    
    % Creating the start point (green color)
    x_start = r * cos(th) + x_start;
    y_start = r * sin(th) + y_start;
    plot(x_start, y_start, 'g', 'LineWidth', 5);
    hold on;
    
    % Creating the end point (red color)
    x_end = r * cos(th) + x_end;
    y_end = r * sin(th) + y_end;
    plot(x_end, y_end, 'r', 'LineWidth', 5);
    hold on;
    
    % Creating the start point (green color)
    h = plot(x_circle, y_circle, x_line, y_line, 'LineWidth', 3);
    hold off;
end