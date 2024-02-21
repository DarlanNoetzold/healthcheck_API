import numpy as np
cimport numpy as cnp

def prepare_data(cnp.ndarray[cnp.float64_t, ndim=1] series, int n=10):
    cdef int i
    cdef list X = []
    cdef list y = []
    # Verifica se a série tem mais de n elementos
    if series.shape[0] > n:
        for i in range(series.shape[0] - n):
            X.append(series[i:i+n])
            y.append(series[i+n])
    return np.array(X, dtype=np.float64), np.array(y, dtype=np.float64)
