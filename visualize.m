% Mobile Robot Visualization

function visualize(xin, yin, angle)
    r=4;
    theta = linspace(0,2*pi);
    x = r*cos(theta) + xin;
    y = r*sin(theta) + yin;

    m = tan(degtorad(angle));
    l1 = linspace(xin, xin+5, 6);
    l2 = linspace(yin, yin*m, 6);
    plot(l1, l2, x, y, 'r', 'LineWidth', 5);

    xlabel('X axix');
    ylabel('Y axis');
    grid on;
    title('Simulating Mobile Robot');
    axis([0 50 0 50])
end













