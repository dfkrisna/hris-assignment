$(document).ready(function() {
    $('#tabel-pengguna').DataTable();
    $('#tabel-penugasan-periode-ini').DataTable();
    $('#tabel-riwayat-penugasan').DataTable();
    $('#tabel-penilaian-mandiri').DataTable();
    // $('#matrix-pmo').DataTable();
    $('#sub-proyek').DataTable();
    $('#table-rekomendasi').DataTable();
    $('#tabel-anggota-divisi').DataTable();
    $('#tabel-finalisasi').DataTable();
    $('#tabel-rf').DataTable();
    $('#tabel-detailkar').DataTable();
    $('#tabel-listproyek').DataTable();
    $('#rekan-seproyek').DataTable();
    $('#rekap-proyek').DataTable();
    $('#pmo-proyek').DataTable();
    $('#rekap-riwayat').DataTable( {
        "order": [[ 4, "desc" ]]
    });

    $('.table-mpp').DataTable();

    var table = $('.table-matrix').removeAttr('width').DataTable( {
        scrollY:        "300px",
        scrollX:        "100%",
        scrollCollapse: true,
        fixedColumns:   {
            leftColumns: 1
        },columnDefs: [
            { width: 300, targets: 0 }
        ],
        "order": [[ 1, "desc" ]]
    } );

    $('#feedbackModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var header = button.data('title'); // Extract info from data-* attributes
        var feedback = button.data('feedback');
        var rating = button.data('rating');
        var namaRekan = button.data('namarekan');
        var kodeProyek = button.data('kodeproyek');
        var idRekan = button.data('idrekan');

        var modal = $(this);
        if(feedback != 'Belum memberikan Feedback'){
            modal.find('.modal-body textarea').val(feedback);
        }else{
            modal.find('.modal-body textarea').val("");
        }

        if(feedback != 0){
            modal.find('.modal-body #rating').val(rating);
        }else {
            modal.find('.modal-body #rating').val(0);
        }


        modal.find('.modal-body #namaRekan').val(namaRekan);
        modal.find('.modal-body #idRekan').val(idRekan);
        modal.find('.modal-body #kodeProyek').val(kodeProyek);

    })

} );