function [new_locs] = find_symbol(dat,preamble, fs, r, visual_debug, mode)
    f_begin = 1000;
    f_end = 4000;
    N_pre = length(preamble);
    %% band-pass filter 
    filter_order = 96;
    wn = [(800)/(fs/2), (4000)/(fs/2)];    
    b = fir1(filter_order, wn, 'bandpass');  
    y_after_fir=filter(b,1,dat);
    delay_fir = filter_order/2;
    block_fir = y_after_fir(delay_fir:end);
    filt = block_fir;
    offset=10e3;
    filt=filt(offset:end);
    [acor,lag]=xcorr(filt,preamble);
    threshold = 0.5;
    %% sync use cross correlation
    [pks,locs]=findpeaks(acor,'MinPeakHeight',threshold,'MinPeakDistance',2000);
    [~,idx]=sort(pks,'descend');
    locs=locs(idx);
    locs=locs(1:min(10,length(locs)));
    pks=acor(locs);
    

    [locs,idx]=sort(locs);
    pks=pks(idx);
    win_size = 1000;
    max_p = 100; %130 5 - 87;
    threshold = 0.5;
    mets=[];
    locs2=[];
    pks2=[];

    for l=1:length(locs)
       if locs(l)- win_size < 1
           begin_index = 0;
       else
           begin_index = locs(l)- win_size;
       end

       if locs(l) + win_size > length(acor)
           end_index = length(acor);
       else
           end_index= locs(l) + win_size;
       end
       seg=acor(begin_index:end_index);
       met=length(find(seg > pks(l)*threshold));

       if met <= max_p
          mets=[mets met];
          locs2=[locs2 locs(l)];
          pks2=[pks2 acor(locs(l))];
       end
       
    end
    %% remove some peaks caused by sparkle noise
    if length(mets)>4
        npks=sort(pks2,'descend');
        remove_idx=[];
        for j=flip(npks(5:end))
            remove_idx=[remove_idx find(pks2==j)];
        end
        locs2(remove_idx)=[];
    end

    %% sync use channel estimation
    seek_back = 960;
    p_threshold = 0.4;
    new_locs1 = seekback(acor, seek_back, acor(locs2(mode)), locs2(mode), p_threshold);
    offset2 = 300;
    seg1 = filt(lag(new_locs1)-offset2+1:lag(new_locs1)-offset2+N_pre);
    Y = fft(seg1);
    X = fft(preamble);
    H = complex(zeros(N_pre,1));
    delta_f = fs/N_pre;
    begin_i = round(f_begin/delta_f);
    end_i = round(f_end/delta_f);
    H(begin_i:end_i) = Y(begin_i:end_i)./(X(begin_i:end_i));
    h = ifft(H);

    [ h, path1, path1_new, noise_level] = channe_look_back( h, 0.35, 5, 1); % find the earlist channel peak
    new_locs = lag(new_locs1)-offset2+path1_new+offset;

end