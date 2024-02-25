from setuptools import setup, Extension
from Cython.Build import cythonize
import numpy

extensions = [
    Extension("data_preparation", ["data_preparation.pyx"],
              include_dirs=[numpy.get_include()]),
    Extension("extract_dataset_from_database", ["extract_dataset_from_database.pyx"],
              include_dirs=[numpy.get_include()]),
]

setup(
    name="MyProject",
    ext_modules=cythonize(extensions),
)
