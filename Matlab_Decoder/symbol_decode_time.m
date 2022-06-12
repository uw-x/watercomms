function [dat_gt, dat_pred, fft_gt, fft_pred, acc] = symbol_decode_time(symbol_gt, symbol_pred, nbin1, nbin2, valid_carrier)
    
% figure
%     hold on
%     plot(symbol_pred, 'b-o')
%     plot(symbol_gt, 'r--x')

    fft_gt=fft(symbol_gt);
    fft_pred = fft(symbol_pred);


    % strip zeros
    fft_gt=fft_gt(valid_carrier);
    fft_pred=fft_pred(valid_carrier);

    dat_gt=pskdemod(fft_gt,2);
    dat_pred=pskdemod(fft_pred,2);
    acc = sum(dat_gt==dat_pred)/length(dat_gt);
   
end