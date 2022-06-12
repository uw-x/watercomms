function diffs=hmap(dats_pred,dats_gt, name, r)

    diffs=(dats_pred==dats_gt);
    
    diffs2=int8(diffs)*255;
    ff=figure(300+r);
    hold on
    ff.Position = [100 100 1600 300];
    x = 1:size(diffs,1);
    y = 1:size(diffs,2);
    image(y,x,diffs2);
    %heatmap(diffs);
    ylabel('Subcarrier #')
    xlabel('Symbol #')
    title(strcat('The visualization of the error bits across symbol and subcarrier', name))
    %title(sprintf('%d %d [%d %d]',Ns,Gi,f_range(1),f_range(2)))
    %saveas(ff,sprintf('hmap_%d_%d_%d_%d.png',Ns,Gi,f_range(1),f_range(2)))

end