function [error_bit, decode_bit] = viterbi_decode(data_to_decode,code_rate, decode_data_gt)

    assert(mod(length(data_to_decode)*code_rate(1), code_rate(2)) == 0)
    
    %% begin the viterbi decode  
    ConstraintLength = 7;
    if(code_rate(1) ==1  && code_rate(2) == 2)
        track_depth = 12;%5*(ConstraintLength - 1);
        vDec = comm.ViterbiDecoder('InputFormat','Hard', 'TracebackDepth', track_depth);
    elseif(code_rate(1) ==2  && code_rate(2) == 3)
        pPatternMat = [1 0 ;1 1];
        pPatternVec = reshape(pPatternMat,4,1);
        track_depth = 16; %;7.5*(ConstraintLength - 1);
        vDec = comm.ViterbiDecoder('InputFormat','Hard','PuncturePatternSource','Property', ...
            'PuncturePattern',pPatternVec, 'TracebackDepth', track_depth);
    elseif(code_rate(1) ==3  && code_rate(2) == 4)
        pPatternMat = [1 0 1;1 1 0];
        pPatternVec = reshape(pPatternMat,6,1);
        track_depth = 10*(ConstraintLength - 1);
        vDec = comm.ViterbiDecoder('InputFormat','Hard','PuncturePatternSource','Property', ...
            'PuncturePattern',pPatternVec, 'TracebackDepth', track_depth);

    end
   
    error = comm.ErrorRate('ReceiveDelay',vDec.TracebackDepth);
    decode_data = vDec(data_to_decode);

    errors = error(decode_data_gt,decode_data);
    %errors = error(decode_data_gt(1:Compare_bit),decode_data(1:Compare_bit));
    error_bit= errors(2);
    decode_bit = errors(3);  
end