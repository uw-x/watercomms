function [ new_locs ] = seekback(h, seek_num, peak, peak_id, p_threshold)
    p_threshold = peak*p_threshold; %0.65
    new_locs = peak_id;
    for j = seek_num : -1 : 0
        l_new = peak_id - j;
        if(h(l_new) > p_threshold)
            new_locs = l_new;
            break;
        end
    end
end

