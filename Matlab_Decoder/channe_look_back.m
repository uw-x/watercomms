function [ h, path1, path1_new, noise_level] = channe_look_back( h, p_threshold2,min_dis, auto_threshold )
    h = abs(h); 
    h =h/max(h);
    noise_level = mean(h(end-500:end));
    noise_level = noise_level*1.5+0.15;
    if(auto_threshold)
        p_threshold2 = noise_level;
    end
    [h_peak1, path1] = max(h);

    [~, hid1] = findpeaks(h(1:path1+10),'MinPeakHeight', p_threshold2, 'MinPeakDistance',min_dis);
    path1_new = hid1(1);
  
end

